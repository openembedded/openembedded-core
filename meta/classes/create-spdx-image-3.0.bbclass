#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: GPL-2.0-only
#
# SPDX image tasks

SPDX_ROOTFS_PACKAGES = "${SPDXDIR}/rootfs-packages.json"
SPDXIMAGEDEPLOYDIR = "${SPDXDIR}/image-deploy"
SPDXROOTFSDEPLOY = "${SPDXDIR}/rootfs-deploy"

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
    import oe.spdx30_tasks
    oe.spdx30_tasks.create_rootfs_spdx(d)
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
    import oe.spdx30_tasks
    oe.spdx30_tasks.create_image_spdx(d)
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
    import oe.spdx30_tasks
    oe.spdx30_tasks.create_image_sbom_spdx(d)
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
    import oe.spdx30_tasks
    spdx_work_dir = Path(d.getVar('SPDXSDKWORK'))

    oe.spdx30_tasks.sdk_create_spdx(d, "host", spdx_work_dir, d.getVar("TOOLCHAIN_OUTPUTNAME"))
}

python sdk_target_create_spdx() {
    from pathlib import Path
    import oe.spdx30_tasks
    spdx_work_dir = Path(d.getVar('SPDXSDKWORK'))

    oe.spdx30_tasks.sdk_create_spdx(d, "target", spdx_work_dir, d.getVar("TOOLCHAIN_OUTPUTNAME"))
}

python sdk_ext_host_create_spdx() {
    from pathlib import Path
    import oe.spdx30_tasks
    spdx_work_dir = Path(d.getVar('SPDXSDKEXTWORK'))

    # TODO: This doesn't seem to work
    oe.spdx30_tasks.sdk_create_spdx(d, "host", spdx_work_dir, d.getVar("TOOLCHAINEXT_OUTPUTNAME"))
}

python sdk_ext_target_create_spdx() {
    from pathlib import Path
    import oe.spdx30_tasks
    spdx_work_dir = Path(d.getVar('SPDXSDKEXTWORK'))

    # TODO: This doesn't seem to work
    oe.spdx30_tasks.sdk_create_spdx(d, "target", spdx_work_dir, d.getVar("TOOLCHAINEXT_OUTPUTNAME"))
}


python sdk_create_sbom() {
    from pathlib import Path
    import oe.spdx30_tasks
    sdk_deploydir = Path(d.getVar("SDKDEPLOYDIR"))
    spdx_work_dir = Path(d.getVar('SPDXSDKWORK'))

    oe.spdx30_tasks.create_sdk_sbom(d, sdk_deploydir, spdx_work_dir, d.getVar("TOOLCHAIN_OUTPUTNAME"))
}

python sdk_ext_create_sbom() {
    from pathlib import Path
    import oe.spdx30_tasks
    sdk_deploydir = Path(d.getVar("SDKEXTDEPLOYDIR"))
    spdx_work_dir = Path(d.getVar('SPDXSDKEXTWORK'))

    oe.spdx30_tasks.create_sdk_sbom(d, sdk_deploydir, spdx_work_dir, d.getVar("TOOLCHAINEXT_OUTPUTNAME"))
}

