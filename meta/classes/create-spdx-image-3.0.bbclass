#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: GPL-2.0-only
#
# SPDX image tasks

SPDX_ROOTFS_PACKAGES = "${SPDXDIR}/rootfs-packages.json"
SPDXIMAGEDEPLOYDIR = "${SPDXDIR}/image-deploy"
SPDXROOTFSDEPLOY = "${SPDXDIR}/rootfs-deploy"

def collect_build_package_inputs(d, objset, build, packages):
    import oe.spdx_common
    providers = oe.spdx_common.collect_package_providers(d)

    build_deps = set()

    for name in sorted(packages.keys()):
        if name not in providers:
            bb.fatal("Unable to find SPDX provider for '%s'" % name)

        pkg_name, pkg_hashfn = providers[name]

        # Copy all of the package SPDX files into the Sbom elements
        pkg_spdx, _ = oe.sbom30.find_root_obj_in_jsonld(
            d,
            "packages",
            pkg_name,
            oe.spdx30.software_Package,
            software_primaryPurpose=oe.spdx30.software_SoftwarePurpose.install,
        )
        build_deps.add(pkg_spdx._id)

    if build_deps:
        objset.new_scoped_relationship(
            [build],
            oe.spdx30.RelationshipType.hasInputs,
            oe.spdx30.LifecycleScopeType.build,
            sorted(list(build_deps)),
        )


python spdx_collect_rootfs_packages() {
    import json
    from pathlib import Path
    from oe.rootfs import image_list_installed_packages

    root_packages_file = Path(d.getVar("SPDX_ROOTFS_PACKAGES"))

    packages = image_list_installed_packages(d)
    if not packages:
        packages = {}

    root_packages_file.parent.mkdir(parents=True, exist_ok=True)
    with root_packages_file.open("w") as f:
        json.dump(packages, f)
}
ROOTFS_POSTUNINSTALL_COMMAND =+ "spdx_collect_rootfs_packages"

python do_create_rootfs_spdx() {
    import json
    from pathlib import Path
    import oe.spdx30
    import oe.sbom30
    from datetime import datetime

    deploy_dir_spdx = Path(d.getVar("DEPLOY_DIR_SPDX"))
    deploydir = Path(d.getVar("SPDXROOTFSDEPLOY"))
    root_packages_file = Path(d.getVar("SPDX_ROOTFS_PACKAGES"))
    image_basename = d.getVar("IMAGE_BASENAME")
    machine = d.getVar("MACHINE")

    with root_packages_file.open("r") as f:
        packages = json.load(f)

    objset = oe.sbom30.ObjectSet.new_objset(d, "%s-%s" % (image_basename, machine))

    rootfs = objset.add_root(oe.spdx30.software_Package(
        _id=objset.new_spdxid("rootfs", image_basename),
        creationInfo=objset.doc.creationInfo,
        name=image_basename,
        software_primaryPurpose=oe.spdx30.software_SoftwarePurpose.archive,
    ))
    set_timestamp_now(d, rootfs, "builtTime")

    rootfs_build = objset.add_root(objset.new_task_build("rootfs", "rootfs"))
    set_timestamp_now(d, rootfs_build, "build_buildEndTime")

    objset.new_scoped_relationship(
        [rootfs_build],
        oe.spdx30.RelationshipType.hasOutputs,
        oe.spdx30.LifecycleScopeType.build,
        [rootfs],
    )

    collect_build_package_inputs(d, objset, rootfs_build, packages)

    oe.sbom30.write_recipe_jsonld_doc(d, objset, "rootfs", deploydir)
}
addtask do_create_rootfs_spdx after do_rootfs before do_image
SSTATETASKS += "do_create_rootfs_spdx"
do_create_rootfs_spdx[sstate-inputdirs] = "${SPDXROOTFSDEPLOY}"
do_create_rootfs_spdx[sstate-outputdirs] = "${DEPLOY_DIR_SPDX}"
do_create_rootfs_spdx[recrdeptask] += "do_create_spdx do_create_package_spdx"
do_create_rootfs_spdx[cleandirs] += "${SPDXROOTFSDEPLOY}"

python do_create_rootfs_spdx_setscene() {
    sstate_setscene(d)
}
addtask do_create_rootfs_spdx_setscene

python do_create_image_spdx() {
    import oe.spdx30
    import oe.sbom30
    import json
    from pathlib import Path

    image_deploy_dir = Path(d.getVar('IMGDEPLOYDIR'))
    manifest_path = Path(d.getVar("IMAGE_OUTPUT_MANIFEST"))
    spdx_work_dir = Path(d.getVar('SPDXIMAGEWORK'))

    image_basename = d.getVar('IMAGE_BASENAME')
    machine = d.getVar("MACHINE")

    objset = oe.sbom30.ObjectSet.new_objset(d, "%s-%s" % (image_basename, machine))

    with manifest_path.open("r") as f:
        manifest = json.load(f)

    builds = []
    for task in manifest:
        imagetype = task["imagetype"]
        taskname = task["taskname"]

        image_build = objset.add_root(objset.new_task_build(taskname, "image/%s" % imagetype))
        set_timestamp_now(d, image_build, "build_buildEndTime")
        builds.append(image_build)

        artifacts = []

        for image in task["images"]:
            image_filename = image["filename"]
            image_path = image_deploy_dir / image_filename
            a = objset.add_root(oe.spdx30.software_File(
                _id=objset.new_spdxid("image", image_filename),
                creationInfo=objset.doc.creationInfo,
                name=image_filename,
                verifiedUsing=[
                    oe.spdx30.Hash(
                        algorithm=oe.spdx30.HashAlgorithm.sha256,
                        hashValue=bb.utils.sha256_file(image_path),
                    )
                ]
            ))
            set_purposes(d, a, "SPDX_IMAGE_PURPOSE:%s" % imagetype, "SPDX_IMAGE_PURPOSE")
            set_timestamp_now(d, a, "builtTime")

            artifacts.append(a)

        if artifacts:
            objset.new_scoped_relationship(
                [image_build],
                oe.spdx30.RelationshipType.hasOutputs,
                oe.spdx30.LifecycleScopeType.build,
                artifacts,
            )

    if builds:
        rootfs_image, _ = oe.sbom30.find_root_obj_in_jsonld(
            d,
            "rootfs",
            "%s-%s" % (image_basename, machine),
            oe.spdx30.software_Package,
            # TODO: Should use a purpose to filter here?
        )
        objset.new_scoped_relationship(
            builds,
            oe.spdx30.RelationshipType.hasInputs,
            oe.spdx30.LifecycleScopeType.build,
            [rootfs_image._id],
        )

    objset.add_aliases()
    objset.link()
    oe.sbom30.write_recipe_jsonld_doc(d, objset, "image", spdx_work_dir)
}
addtask do_create_image_spdx after do_image_complete do_create_rootfs_spdx before do_build
SSTATETASKS += "do_create_image_spdx"
SSTATE_SKIP_CREATION:task-combine-image-type-spdx = "1"
do_create_image_spdx[sstate-inputdirs] = "${SPDXIMAGEWORK}"
do_create_image_spdx[sstate-outputdirs] = "${DEPLOY_DIR_SPDX}"
do_create_image_spdx[cleandirs] = "${SPDXIMAGEWORK}"
do_create_image_spdx[dirs] = "${SPDXIMAGEWORK}"

python do_create_image_spdx_setscene() {
    sstate_setscene(d)
}
addtask do_create_image_spdx_setscene


python do_create_image_sbom_spdx() {
    import os
    from pathlib import Path
    import oe.spdx30
    import oe.sbom30

    image_name = d.getVar("IMAGE_NAME")
    image_basename = d.getVar("IMAGE_BASENAME")
    image_link_name = d.getVar("IMAGE_LINK_NAME")
    imgdeploydir = Path(d.getVar("SPDXIMAGEDEPLOYDIR"))
    machine = d.getVar("MACHINE")

    spdx_path = imgdeploydir / (image_name + ".spdx.json")

    root_elements = []

    # TODO: Do we need to add the rootfs or are the image files sufficient?
    rootfs_image, _ = oe.sbom30.find_root_obj_in_jsonld(
        d,
        "rootfs",
        "%s-%s" % (image_basename, machine),
        oe.spdx30.software_Package,
        # TODO: Should use a purpose here?
    )
    root_elements.append(rootfs_image._id)

    image_objset, _ = oe.sbom30.find_jsonld(d, "image", "%s-%s" % (image_basename, machine), required=True)
    for o in image_objset.foreach_root(oe.spdx30.software_File):
        root_elements.append(o._id)

    objset, sbom = oe.sbom30.create_sbom(d, image_name, root_elements)

    oe.sbom30.write_jsonld_doc(d, objset, spdx_path)

    def make_image_link(target_path, suffix):
        if image_link_name:
            link = imgdeploydir / (image_link_name + suffix)
            if link != target_path:
                link.symlink_to(os.path.relpath(target_path, link.parent))

    make_image_link(spdx_path, ".spdx.json")
}
addtask do_create_image_sbom_spdx after do_create_rootfs_spdx do_create_image_spdx before do_build
SSTATETASKS += "do_create_image_sbom_spdx"
SSTATE_SKIP_CREATION:task-create-image-sbom = "1"
do_create_image_sbom_spdx[sstate-inputdirs] = "${SPDXIMAGEDEPLOYDIR}"
do_create_image_sbom_spdx[sstate-outputdirs] = "${DEPLOY_DIR_IMAGE}"
do_create_image_sbom_spdx[stamp-extra-info] = "${MACHINE_ARCH}"
do_create_image_sbom_spdx[cleandirs] = "${SPDXIMAGEDEPLOYDIR}"
do_create_image_sbom_spdx[recrdeptask] += "do_create_spdx do_create_package_spdx"

python do_create_image_sbom_spdx_setscene() {
    sstate_setscene(d)
}
addtask do_create_image_sbom_spdx_setscene

do_populate_sdk[recrdeptask] += "do_create_spdx do_create_package_spdx"
do_populate_sdk[cleandirs] += "${SPDXSDKWORK}"
do_populate_sdk[postfuncs] += "sdk_create_sbom"
POPULATE_SDK_POST_HOST_COMMAND:append:task-populate-sdk = " sdk_host_create_spdx"
POPULATE_SDK_POST_TARGET_COMMAND:append:task-populate-sdk = " sdk_target_create_spdx"

do_populate_sdk_ext[recrdeptask] += "do_create_spdx do_create_package_spdx"
do_populate_sdk_ext[cleandirs] += "${SPDXSDKEXTWORK}"
do_populate_sdk_ext[postfuncs] += "sdk_ext_create_sbom"
POPULATE_SDK_POST_HOST_COMMAND:append:task-populate-sdk-ext = " sdk_ext_host_create_spdx"
POPULATE_SDK_POST_TARGET_COMMAND:append:task-populate-sdk-ext = " sdk_ext_target_create_spdx"

python sdk_host_create_spdx() {
    from pathlib import Path
    spdx_work_dir = Path(d.getVar('SPDXSDKWORK'))

    sdk_create_spdx(d, "host", spdx_work_dir, d.getVar("TOOLCHAIN_OUTPUTNAME"))
}

python sdk_target_create_spdx() {
    from pathlib import Path
    spdx_work_dir = Path(d.getVar('SPDXSDKWORK'))

    sdk_create_spdx(d, "target", spdx_work_dir, d.getVar("TOOLCHAIN_OUTPUTNAME"))
}

python sdk_ext_host_create_spdx() {
    from pathlib import Path
    spdx_work_dir = Path(d.getVar('SPDXSDKEXTWORK'))

    # TODO: This doesn't seem to work
    sdk_create_spdx(d, "host", spdx_work_dir, d.getVar("TOOLCHAINEXT_OUTPUTNAME"))
}

python sdk_ext_target_create_spdx() {
    from pathlib import Path
    spdx_work_dir = Path(d.getVar('SPDXSDKEXTWORK'))

    # TODO: This doesn't seem to work
    sdk_create_spdx(d, "target", spdx_work_dir, d.getVar("TOOLCHAINEXT_OUTPUTNAME"))
}

def sdk_create_spdx(d, sdk_type, spdx_work_dir, toolchain_outputname):
    from pathlib import Path
    from oe.sdk import sdk_list_installed_packages
    import oe.spdx30
    import oe.sbom30
    from datetime import datetime

    sdk_name = toolchain_outputname + "-" + sdk_type
    sdk_packages = sdk_list_installed_packages(d, sdk_type == "target")

    objset = oe.sbom30.ObjectSet.new_objset(d, sdk_name)

    sdk_rootfs = objset.add_root(oe.spdx30.software_Package(
        _id=objset.new_spdxid("sdk-rootfs", sdk_name),
        creationInfo=objset.doc.creationInfo,
        name=sdk_name,
        software_primaryPurpose=oe.spdx30.software_SoftwarePurpose.archive,
    ))
    set_timestamp_now(d, sdk_rootfs, "builtTime")

    sdk_build = objset.add_root(objset.new_task_build("sdk-rootfs", "sdk-rootfs"))
    set_timestamp_now(d, sdk_build, "build_buildEndTime")

    objset.new_scoped_relationship(
        [sdk_build],
        oe.spdx30.RelationshipType.hasOutputs,
        oe.spdx30.LifecycleScopeType.build,
        [sdk_rootfs],
    )

    collect_build_package_inputs(d, objset, sdk_build, sdk_packages)

    objset.add_aliases()
    oe.sbom30.write_jsonld_doc(d, objset, spdx_work_dir / "sdk-rootfs.spdx.json")

python sdk_create_sbom() {
    from pathlib import Path
    sdk_deploydir = Path(d.getVar("SDKDEPLOYDIR"))
    spdx_work_dir = Path(d.getVar('SPDXSDKWORK'))

    create_sdk_sbom(d, sdk_deploydir, spdx_work_dir, d.getVar("TOOLCHAIN_OUTPUTNAME"))
}

python sdk_ext_create_sbom() {
    from pathlib import Path
    sdk_deploydir = Path(d.getVar("SDKEXTDEPLOYDIR"))
    spdx_work_dir = Path(d.getVar('SPDXSDKEXTWORK'))

    create_sdk_sbom(d, sdk_deploydir, spdx_work_dir, d.getVar("TOOLCHAINEXT_OUTPUTNAME"))
}

def create_sdk_sbom(d, sdk_deploydir, spdx_work_dir, toolchain_outputname):
    import oe.spdx30
    import oe.sbom30
    from pathlib import Path
    from datetime import datetime

    # Load the document written earlier
    rootfs_objset = oe.sbom30.load_jsonld(d, spdx_work_dir / "sdk-rootfs.spdx.json", required=True)

    # Create a new build for the SDK installer
    sdk_build = rootfs_objset.new_task_build("sdk-populate", "sdk-populate")
    set_timestamp_now(d, sdk_build, "build_buildEndTime")

    rootfs = rootfs_objset.find_root(oe.spdx30.software_Package)
    if rootfs is None:
        bb.fatal("Unable to find rootfs artifact")

    rootfs_objset.new_scoped_relationship(
        [sdk_build],
        oe.spdx30.RelationshipType.hasInputs,
        oe.spdx30.LifecycleScopeType.build,
        [rootfs]
    )

    files = set()
    root_files = []

    # NOTE: os.walk() doesn't return symlinks
    for dirpath, dirnames, filenames in os.walk(sdk_deploydir):
        for fn in filenames:
            fpath = Path(dirpath) / fn
            if not fpath.is_file() or fpath.is_symlink():
                continue

            relpath = str(fpath.relative_to(sdk_deploydir))

            f = rootfs_objset.new_file(
                rootfs_objset.new_spdxid("sdk-installer", relpath),
                relpath,
                fpath,
            )
            set_timestamp_now(d, f, "builtTime")

            if fn.endswith(".manifest"):
                f.software_primaryPurpose = oe.spdx30.software_SoftwarePurpose.manifest
            elif fn.endswith(".testdata.json"):
                f.software_primaryPurpose = oe.spdx30.software_SoftwarePurpose.configuration
            else:
                set_purposes(d, f, "SPDX_SDK_PURPOSE")
                root_files.append(f)

            files.add(f)

    if files:
        rootfs_objset.new_scoped_relationship(
            [sdk_build],
            oe.spdx30.RelationshipType.hasOutputs,
            oe.spdx30.LifecycleScopeType.build,
            files,
        )
    else:
        bb.warn(f"No SDK output files found in {sdk_deploydir}")

    objset, sbom = oe.sbom30.create_sbom(d, toolchain_outputname, sorted(list(files)), [rootfs_objset])

    oe.sbom30.write_jsonld_doc(d, objset, sdk_deploydir / (toolchain_outputname + ".spdx.json"))

