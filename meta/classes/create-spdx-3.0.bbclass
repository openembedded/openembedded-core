#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: GPL-2.0-only
#

inherit spdx-common

SPDX_VERSION = "3.0.0"

# The list of SPDX profiles generated documents will conform to
SPDX_PROFILES ?= "core build software simpleLicensing security"

SPDX_INCLUDE_BUILD_VARIABLES ??= "0"
SPDX_INCLUDE_BUILD_VARIABLES[doc] = "If set to '1', the bitbake variables for a \
    recipe will be included in the Build object. This will most likely result \
    in non-reproducible SPDX output"

SPDX_INCLUDE_BITBAKE_PARENT_BUILD ??= "0"
SPDX_INCLUDE_BITBAKE_PARENT_BUILD[doc] = "Report the parent invocation of bitbake \
    for each Build object. This allows you to know who invoked bitbake to perform \
    a build, but will result in non-reproducible SPDX output."

SPDX_PACKAGE_ADDITIONAL_PURPOSE ?= ""
SPDX_PACKAGE_ADDITIONAL_PURPOSE[doc] = "The list of additional purposes to assign to \
    the generated packages for a recipe. The primary purpose is always `install`. \
    Packages overrides are allowed to override the additional purposes for \
    individual packages."

SPDX_IMAGE_PURPOSE ?= "filesystemImage"
SPDX_IMAGE_PURPOSE[doc] = "The list of purposes to assign to the generated images. \
    The first listed item will be the Primary Purpose and all additional items will \
    be added as additional purposes"

SPDX_SDK_PURPOSE ?= "install"
SPDX_SDK_PURPOSE[doc] = "The list of purposes to assign to the generate SDK installer. \
    The first listed item will be the Primary Purpose and all additional items will \
    be added as additional purposes"

SPDX_INCLUDE_VEX ??= "current"
SPDX_INCLUDE_VEX[doc] = "Controls what VEX information is in the output. Set to \
    'none' to disable all VEX data. Set to 'current' to only include VEX data \
    for vulnerabilities not already fixed in the upstream source code \
    (recommended). Set  to 'all' to get all known historical vulnerabilities, \
    including those already fixed upstream (warning: This can be large and \
    slow)."

SPDX_INCLUDE_TIMESTAMPS ?= "0"
SPDX_INCLUDE_TIMESTAMPS[doc] = "Include time stamps in SPDX output. This is \
    useful if you want to know when artifacts were produced and when builds \
    occurred, but will result in non-reproducible SPDX output"

SPDX_IMPORTS ??= ""
SPDX_IMPORTS[doc] = "SPDX_IMPORTS is the base variable that describes how to \
    reference external SPDX ids. Each import is defined as a key in this \
    variable with a suffix to describe to as a suffix to look up more \
    information about the import. Each key can have the following variables: \
        SPDX_IMPORTS_<key>_spdxid: The Fully qualified SPDX ID of the object \
        SPDX_IMPORTS_<key>_uri: The URI where the SPDX Document that contains \
            the external object can be found. Optional but recommended \
        SPDX_IMPORTS_<key>_hash_<hash>: The Checksum of the SPDX Document that \
            contains the External ID. <hash> must be one the valid SPDX hashing \
            algorithms, as described by the HashAlgorithm vocabulary in the\
            SPDX 3 spec. Optional but recommended"

# Agents
#   Bitbake variables can be used to describe an SPDX Agent that may be used
#   during the build. An Agent is specified using a set of variables which all
#   start with some common base name:
#
#   <BASE>_name: The name of the Agent (required)
#   <BASE>_type: The type of Agent. Must be one of "person", "organization",
#       "software", or "agent" (the default if not specified)
#   <BASE>_comment: The comment for the Agent (optional)
#   <BASE>_id_<ID>: And External Identifier for the Agent. <ID> must be a valid
#       ExternalIdentifierType from the SPDX 3 spec. Commonly, an E-mail address
#       can be specified with <BASE>_id_email
#
#   Alternatively, an Agent can be an external reference by referencing a key
#   in SPDX_IMPORTS like so:
#
#   <BASE>_import = "<key>"
#
#   Finally, the same agent described by another set of agent variables can be
#   referenced by specifying the basename of the variable that should be
#   referenced:
#
#   SPDX_PACKAGE_SUPPLIER_ref = "SPDX_AUTHORS_openembedded"

SPDX_AUTHORS ??= "openembedded"
SPDX_AUTHORS[doc] = "A space separated list of the document authors. Each item \
    is used to name a base variable like SPDX_AUTHORS_<AUTHOR> that \
    describes the author."

SPDX_AUTHORS_openembedded_name = "OpenEmbedded"
SPDX_AUTHORS_openembedded_type = "organization"

SPDX_BUILD_HOST[doc] = "The base variable name to describe the build host on \
    which a build is running. Must be an SPDX_IMPORTS key. Requires \
    SPDX_INCLUDE_BITBAKE_PARENT_BUILD. NOTE: Setting this will result in \
    non-reproducible SPDX output"

SPDX_INVOKED_BY[doc] = "The base variable name to describe the Agent that \
    invoked the build, which builds will link to if specified. Requires \
    SPDX_INCLUDE_BITBAKE_PARENT_BUILD. NOTE: Setting this will likely result in \
    non-reproducible SPDX output"

SPDX_ON_BEHALF_OF[doc] = "The base variable name to describe the Agent on who's \
    behalf the invoking Agent (SPDX_INVOKED_BY) is running the build. Requires \
    SPDX_INCLUDE_BITBAKE_PARENT_BUILD. NOTE: Setting this will likely result in \
    non-reproducible SPDX output"

SPDX_PACKAGE_SUPPLIER[doc] = "The base variable name to describe the Agent who \
    is supplying artifacts produced by the build"


IMAGE_CLASSES:append = " create-spdx-image-3.0"

def set_timestamp_now(d, o, prop):
    from datetime import datetime, timezone

    if d.getVar("SPDX_INCLUDE_TIMESTAMPS") == "1":
        setattr(o, prop, datetime.now(timezone.utc))
    else:
        # Doing this helps to validated that the property actually exists, and
        # also that it is not mandatory
        delattr(o, prop)

set_timestamp_now[vardepsexclude] = "SPDX_INCLUDE_TIMESTAMPS"

def add_license_expression(d, objset, license_expression):
    from pathlib import Path
    import oe.spdx30
    import oe.sbom30

    license_data = d.getVar("SPDX_LICENSE_DATA")
    simple_license_text = {}
    license_text_map = {}
    license_ref_idx = 0

    def add_license_text(name):
        nonlocal objset
        nonlocal simple_license_text

        if name in simple_license_text:
            return simple_license_text[name]

        lic = objset.find_filter(
            oe.spdx30.simplelicensing_SimpleLicensingText,
            name=name,
        )

        if lic is not None:
            simple_license_text[name] = lic
            return lic

        lic = objset.add(oe.spdx30.simplelicensing_SimpleLicensingText(
            _id=objset.new_spdxid("license-text", name),
            creationInfo=objset.doc.creationInfo,
            name=name,
        ))
        simple_license_text[name] = lic

        if name == "PD":
            lic.simplelicensing_licenseText = "Software released to the public domain"
            return lic

        # Seach for the license in COMMON_LICENSE_DIR and LICENSE_PATH
        for directory in [d.getVar('COMMON_LICENSE_DIR')] + (d.getVar('LICENSE_PATH') or '').split():
            try:
                with (Path(directory) / name).open(errors="replace") as f:
                    lic.simplelicensing_licenseText = f.read()
                    return lic

            except FileNotFoundError:
                pass

        # If it's not SPDX or PD, then NO_GENERIC_LICENSE must be set
        filename = d.getVarFlag('NO_GENERIC_LICENSE', name)
        if filename:
            filename = d.expand("${S}/" + filename)
            with open(filename, errors="replace") as f:
                lic.simplelicensing_licenseText = f.read()
                return lic
        else:
            bb.fatal("Cannot find any text for license %s" % name)

    def convert(l):
        nonlocal license_text_map
        nonlocal license_ref_idx

        if l == "(" or l == ")":
            return l

        if l == "&":
            return "AND"

        if l == "|":
            return "OR"

        if l == "CLOSED":
            return "NONE"

        spdx_license = d.getVarFlag("SPDXLICENSEMAP", l) or l
        if spdx_license in license_data["licenses"]:
            return spdx_license

        spdx_license = "LicenseRef-" + l
        if spdx_license not in license_text_map:
            license_text_map[spdx_license] = add_license_text(l)._id

        return spdx_license

    lic_split = license_expression.replace("(", " ( ").replace(")", " ) ").replace("|", " | ").replace("&", " & ").split()
    spdx_license_expression = ' '.join(convert(l) for l in lic_split)

    return objset.new_license_expression(spdx_license_expression, license_text_map)


def add_package_files(d, objset, topdir, get_spdxid, get_purposes, *, archive=None, ignore_dirs=[], ignore_top_level_dirs=[]):
    from pathlib import Path
    import oe.spdx30
    import oe.sbom30

    source_date_epoch = d.getVar("SOURCE_DATE_EPOCH")
    if source_date_epoch:
        source_date_epoch = int(source_date_epoch)

    spdx_files = set()

    file_counter = 1
    for subdir, dirs, files in os.walk(topdir):
        dirs[:] = [d for d in dirs if d not in ignore_dirs]
        if subdir == str(topdir):
            dirs[:] = [d for d in dirs if d not in ignore_top_level_dirs]

        for file in files:
            filepath = Path(subdir) / file
            if filepath.is_symlink() or not filepath.is_file():
                continue

            bb.debug(1, "Adding file %s to %s" % (filepath, objset.doc._id))

            filename = str(filepath.relative_to(topdir))
            file_purposes = get_purposes(filepath)

            spdx_file = objset.new_file(
                get_spdxid(file_counter),
                filename,
                filepath,
                purposes=file_purposes,
            )
            spdx_files.add(spdx_file)

            if oe.spdx30.software_SoftwarePurpose.source in file_purposes:
                objset.scan_declared_licenses(spdx_file, filepath)

            if archive is not None:
                with filepath.open("rb") as f:
                    info = archive.gettarinfo(fileobj=f)
                    info.name = filename
                    info.uid = 0
                    info.gid = 0
                    info.uname = "root"
                    info.gname = "root"

                    if source_date_epoch is not None and info.mtime > source_date_epoch:
                        info.mtime = source_date_epoch

                    archive.addfile(info, f)

            file_counter += 1

    return spdx_files


def get_package_sources_from_debug(d, package, package_files, sources, source_hash_cache):
    from pathlib import Path
    import oe.packagedata

    def file_path_match(file_path, pkg_file):
        if file_path.lstrip("/") == pkg_file.name.lstrip("/"):
            return True

        for e in pkg_file.extension:
            if isinstance(e, oe.sbom30.OEFileNameAliasExtension):
                for a in e.aliases:
                    if file_path.lstrip("/") == a.lstrip("/"):
                        return True

        return False

    debug_search_paths = [
        Path(d.getVar('PKGD')),
        Path(d.getVar('STAGING_DIR_TARGET')),
        Path(d.getVar('STAGING_DIR_NATIVE')),
        Path(d.getVar('STAGING_KERNEL_DIR')),
    ]

    pkg_data = oe.packagedata.read_subpkgdata_extended(package, d)

    if pkg_data is None:
        return

    dep_source_files = set()

    for file_path, file_data in pkg_data["files_info"].items():
        if not "debugsrc" in file_data:
            continue

        if not any(file_path_match(file_path, pkg_file) for pkg_file in package_files):
            bb.fatal("No package file found for %s in %s; SPDX found: %s" % (str(file_path), package,
                " ".join(p.name for p in package_files)))
            continue

        for debugsrc in file_data["debugsrc"]:
            for search in debug_search_paths:
                if debugsrc.startswith("/usr/src/kernel"):
                    debugsrc_path = search / debugsrc.replace('/usr/src/kernel/', '')
                else:
                    debugsrc_path = search / debugsrc.lstrip("/")

                if debugsrc_path in source_hash_cache:
                    file_sha256 = source_hash_cache[debugsrc_path]
                    if file_sha256 is None:
                        continue
                else:
                    if not debugsrc_path.exists():
                        source_hash_cache[debugsrc_path] = None
                        continue

                    file_sha256 = bb.utils.sha256_file(debugsrc_path)
                    source_hash_cache[debugsrc_path] = file_sha256

                if file_sha256 in sources:
                    source_file = sources[file_sha256]
                    dep_source_files.add(source_file)
                else:
                    bb.debug(1, "Debug source %s with SHA256 %s not found in any dependency" % (str(debugsrc_path), file_sha256))
                break
            else:
                bb.debug(1, "Debug source %s not found" % debugsrc)

    return dep_source_files

get_package_sources_from_debug[vardepsexclude] += "STAGING_KERNEL_DIR"

def collect_dep_objsets(d, build):
    import json
    from pathlib import Path
    import oe.sbom30
    import oe.spdx30
    import oe.spdx_common

    deps = oe.spdx_common.get_spdx_deps(d)

    dep_objsets = []
    dep_builds = set()

    dep_build_spdxids = set()
    for dep in deps:
        bb.debug(1, "Fetching SPDX for dependency %s" % (dep.pn))
        dep_build, dep_objset = oe.sbom30.find_root_obj_in_jsonld(d, "recipes", dep.pn, oe.spdx30.build_Build)
        # If the dependency is part of the taskhash, return it to be linked
        # against. Otherwise, it cannot be linked against because this recipe
        # will not rebuilt if dependency changes
        if dep.in_taskhash:
            dep_objsets.append(dep_objset)

        # The build _can_ be linked against (by alias)
        dep_builds.add(dep_build)

    return dep_objsets, dep_builds

collect_dep_objsets[vardepsexclude] = "SSTATE_ARCHS"

def collect_dep_sources(dep_objsets):
    import oe.spdx30
    import oe.sbom30

    sources = {}
    for objset in dep_objsets:
        # Don't collect sources from native recipes as they
        # match non-native sources also.
        if objset.is_native():
            continue

        bb.debug(1, "Fetching Sources for dependency %s" % (objset.doc.name))

        dep_build = objset.find_root(oe.spdx30.build_Build)
        if not dep_build:
            bb.fatal("Unable to find a build")

        for e in objset.foreach_type(oe.spdx30.Relationship):
            if dep_build is not e.from_:
                continue

            if e.relationshipType != oe.spdx30.RelationshipType.hasInputs:
                continue

            for to in e.to:
                if not isinstance(to, oe.spdx30.software_File):
                    continue

                if to.software_primaryPurpose != oe.spdx30.software_SoftwarePurpose.source:
                    continue

                for v in to.verifiedUsing:
                    if v.algorithm == oe.spdx30.HashAlgorithm.sha256:
                        sources[v.hashValue] = to
                        break
                else:
                    bb.fatal("No SHA256 found for %s in %s" % (to.name, objset.doc.name))

    return sources

def add_download_files(d, objset):
    import oe.patch
    import oe.spdx30
    import os

    inputs = set()

    urls = d.getVar("SRC_URI").split()
    fetch = bb.fetch2.Fetch(urls, d)

    for download_idx, src_uri in enumerate(urls):
        fd = fetch.ud[src_uri]

        for name in fd.names:
            file_name = os.path.basename(fetch.localpath(src_uri))
            if oe.patch.patch_path(src_uri, fetch, '', expand=False):
                primary_purpose = oe.spdx30.software_SoftwarePurpose.patch
            else:
                primary_purpose = oe.spdx30.software_SoftwarePurpose.source

            if fd.type == "file":
                if os.path.isdir(fd.localpath):
                    walk_idx = 1
                    for root, dirs, files in os.walk(fd.localpath):
                        for f in files:
                            f_path = os.path.join(root, f)
                            if os.path.islink(f_path):
                                # TODO: SPDX doesn't support symlinks yet
                                continue

                            file = objset.new_file(
                                objset.new_spdxid("source", str(download_idx + 1), str(walk_idx)),
                                os.path.join(file_name, os.path.relpath(f_path, fd.localpath)),
                                f_path,
                                purposes=[primary_purpose],
                            )

                            inputs.add(file)
                            walk_idx += 1

                else:
                    file = objset.new_file(
                        objset.new_spdxid("source", str(download_idx + 1)),
                        file_name,
                        fd.localpath,
                        purposes=[primary_purpose],
                    )
                    inputs.add(file)

            else:
                uri = fd.type
                proto = getattr(fd, "proto", None)
                if proto is not None:
                    uri = uri + "+" + proto
                uri = uri + "://" + fd.host + fd.path

                if fd.method.supports_srcrev():
                    uri = uri + "@" + fd.revisions[name]

                dl = objset.add(oe.spdx30.software_Package(
                    _id=objset.new_spdxid("source", str(download_idx + 1)),
                    creationInfo=objset.doc.creationInfo,
                    name=file_name,
                    software_primaryPurpose=primary_purpose,
                    software_downloadLocation=uri,
                ))

                if fd.method.supports_checksum(fd):
                    # TODO Need something better than hard coding this
                    for checksum_id in ["sha256", "sha1"]:
                        expected_checksum = getattr(fd, "%s_expected" % checksum_id, None)
                        if expected_checksum is None:
                            continue

                        dl.verifiedUsing.append(
                            oe.spdx30.Hash(
                                algorithm=getattr(oe.spdx30.HashAlgorithm, checksum_id),
                                hashValue=expected_checksum,
                            )
                        )

                inputs.add(dl)

    return inputs


def set_purposes(d, element, *var_names, force_purposes=[]):
    purposes = force_purposes[:]

    for var_name in var_names:
        val = d.getVar(var_name)
        if val:
            purposes.extend(val.split())
            break

    if not purposes:
        bb.warn("No SPDX purposes found in %s" % " ".join(var_names))
        return

    element.software_primaryPurpose = getattr(oe.spdx30.software_SoftwarePurpose, purposes[0])
    element.software_additionalPurpose = [getattr(oe.spdx30.software_SoftwarePurpose, p) for p in purposes[1:]]


python do_create_spdx() {
    import oe.sbom30
    import oe.spdx30
    import oe.spdx_common
    from pathlib import Path
    from contextlib import contextmanager
    import oe.cve_check
    from datetime import datetime

    def set_var_field(var, obj, name, package=None):
        val = None
        if package:
            val = d.getVar("%s:%s" % (var, package))

        if not val:
            val = d.getVar(var)

        if val:
            setattr(obj, name, val)

    deploydir = Path(d.getVar("SPDXDEPLOY"))
    deploy_dir_spdx = Path(d.getVar("DEPLOY_DIR_SPDX"))
    spdx_workdir = Path(d.getVar("SPDXWORK"))
    include_sources = d.getVar("SPDX_INCLUDE_SOURCES") == "1"
    pkg_arch = d.getVar("SSTATE_PKGARCH")
    is_native = bb.data.inherits_class("native", d) or bb.data.inherits_class("cross", d)
    include_vex = d.getVar("SPDX_INCLUDE_VEX")
    if not include_vex in ("none", "current", "all"):
        bb.fatal("SPDX_INCLUDE_VEX must be one of 'none', 'current', 'all'")

    build_objset = oe.sbom30.ObjectSet.new_objset(d, d.getVar("PN"))

    build = build_objset.new_task_build("recipe", "recipe")
    build_objset.doc.rootElement.append(build)

    build_objset.set_is_native(is_native)

    for var in (d.getVar('SPDX_CUSTOM_ANNOTATION_VARS') or "").split():
        new_annotation(
            d,
            build_objset,
            build,
            "%s=%s" % (var, d.getVar(var)),
            oe.spdx30.AnnotationType.other
        )

    build_inputs = set()

    # Add CVEs
    cve_by_status = {}
    if include_vex != "none":
        for cve in (d.getVarFlags("CVE_STATUS") or {}):
            status, detail, description = oe.cve_check.decode_cve_status(d, cve)

            # If this CVE is fixed upstream, skip it unless all CVEs are
            # specified.
            if include_vex != "all" and detail in ("fixed-version", "cpe-stable-backport"):
                bb.debug(1, "Skipping %s since it is already fixed upstream" % cve)
                continue

            cve_by_status.setdefault(status, {})[cve] = (
                build_objset.new_cve_vuln(cve),
                detail,
                description,
            )

    cpe_ids = oe.cve_check.get_cpe_ids(d.getVar("CVE_PRODUCT"), d.getVar("CVE_VERSION"))

    source_files = add_download_files(d, build_objset)
    build_inputs |= source_files

    recipe_spdx_license = add_license_expression(d, build_objset, d.getVar("LICENSE"))
    build_objset.new_relationship(
        source_files,
        oe.spdx30.RelationshipType.hasConcludedLicense,
        [recipe_spdx_license],
    )

    if oe.spdx_common.process_sources(d) and include_sources:
        bb.debug(1, "Adding source files to SPDX")
        oe.spdx_common.get_patched_src(d)

        build_inputs |= add_package_files(
            d,
            build_objset,
            spdx_workdir,
            lambda file_counter: build_objset.new_spdxid("sourcefile", str(file_counter)),
            lambda filepath: [oe.spdx30.software_SoftwarePurpose.source],
            ignore_dirs=[".git"],
            ignore_top_level_dirs=["temp"],
            archive=None,
        )


    dep_objsets, dep_builds = collect_dep_objsets(d, build)
    if dep_builds:
        build_objset.new_scoped_relationship(
            [build],
            oe.spdx30.RelationshipType.dependsOn,
            oe.spdx30.LifecycleScopeType.build,
            sorted(oe.sbom30.get_element_link_id(b) for b in dep_builds),
        )

    debug_source_ids = set()
    source_hash_cache = {}

    # Write out the package SPDX data now. It is not complete as we cannot
    # write the runtime data, so write it to a staging area and a later task
    # will write out the final collection

    # TODO: Handle native recipe output
    if not is_native:
        bb.debug(1, "Collecting Dependency sources files")
        sources = collect_dep_sources(dep_objsets)

        bb.build.exec_func("read_subpackage_metadata", d)

        pkgdest = Path(d.getVar("PKGDEST"))
        for package in d.getVar("PACKAGES").split():
            if not oe.packagedata.packaged(package, d):
                continue

            pkg_name = d.getVar("PKG:%s" % package) or package

            bb.debug(1, "Creating SPDX for package %s" % pkg_name)

            pkg_objset = oe.sbom30.ObjectSet.new_objset(d, pkg_name)

            spdx_package = pkg_objset.add_root(oe.spdx30.software_Package(
                _id=pkg_objset.new_spdxid("package", pkg_name),
                creationInfo=pkg_objset.doc.creationInfo,
                name=pkg_name,
                software_packageVersion=d.getVar("PV"),
            ))
            set_timestamp_now(d, spdx_package, "builtTime")

            set_purposes(
                d,
                spdx_package,
                "SPDX_PACKAGE_ADDITIONAL_PURPOSE:%s" % package,
                "SPDX_PACKAGE_ADDITIONAL_PURPOSE",
                force_purposes=["install"],
            )


            supplier = build_objset.new_agent("SPDX_PACKAGE_SUPPLIER")
            if supplier is not None:
                spdx_package.supplier = supplier if isinstance(supplier, str) else supplier._id

            set_var_field("HOMEPAGE", spdx_package, "software_homePage", package=package)
            set_var_field("SUMMARY", spdx_package, "summary", package=package)
            set_var_field("DESCRIPTION", spdx_package, "description", package=package)

            pkg_objset.new_scoped_relationship(
                [build._id],
                oe.spdx30.RelationshipType.hasOutputs,
                oe.spdx30.LifecycleScopeType.build,
                [spdx_package],
            )

            for cpe_id in cpe_ids:
                spdx_package.externalIdentifier.append(
                    oe.spdx30.ExternalIdentifier(
                        externalIdentifierType=oe.spdx30.ExternalIdentifierType.cpe23,
                        identifier=cpe_id,
                ))

            # TODO: Generate a file for each actual IPK/DEB/RPM/TGZ file
            # generated and link it to the package
            #spdx_package_file = pkg_objset.add(oe.spdx30.software_File(
            #    _id=pkg_objset.new_spdxid("distribution", pkg_name),
            #    creationInfo=pkg_objset.doc.creationInfo,
            #    name=pkg_name,
            #    software_primaryPurpose=spdx_package.software_primaryPurpose,
            #    software_additionalPurpose=spdx_package.software_additionalPurpose,
            #))
            #set_timestamp_now(d, spdx_package_file, "builtTime")

            ## TODO add hashes
            #pkg_objset.new_relationship(
            #    [spdx_package],
            #    oe.spdx30.RelationshipType.hasDistributionArtifact,
            #    [spdx_package_file],
            #)

            # NOTE: licenses live in the recipe collection and are referenced
            # by ID in the package collection(s). This helps reduce duplication
            # (since a lot of packages will have the same license), and also
            # prevents duplicate license SPDX IDs in the packages
            package_license = d.getVar("LICENSE:%s" % package)
            if package_license and package_license != d.getVar("LICENSE"):
                package_spdx_license = add_license_expression(d, build_objset, package_license)
            else:
                package_spdx_license = recipe_spdx_license

            pkg_objset.new_relationship(
                [spdx_package],
                oe.spdx30.RelationshipType.hasConcludedLicense,
                [package_spdx_license._id],
            )

            # NOTE: CVE Elements live in the recipe collection
            all_cves = set()
            for status, cves in cve_by_status.items():
                for cve, items in cves.items():
                    spdx_cve, detail, description = items

                    all_cves.add(spdx_cve._id)

                    if status == "Patched":
                        pkg_objset.new_vex_patched_relationship([spdx_cve._id], [spdx_package])
                    elif status == "Unpatched":
                        pkg_objset.new_vex_unpatched_relationship([spdx_cve._id], [spdx_package])
                    elif status == "Ignored":
                        spdx_vex = pkg_objset.new_vex_ignored_relationship(
                            [spdx_cve._id],
                            [spdx_package],
                            impact_statement=description,
                        )

                        if detail in ("ignored", "cpe-incorrect", "disputed", "upstream-wontfix"):
                            # VEX doesn't have justifications for this
                            pass
                        elif detail in ("not-applicable-config", "not-applicable-platform"):
                            for v in spdx_vex:
                                v.security_justificationType = oe.spdx30.security_VexJustificationType.vulnerableCodeNotPresent
                        else:
                            bb.fatal(f"Unknown detail '{detail}' for ignored {cve}")
                    else:
                        bb.fatal(f"Unknown CVE status {status}")

            if all_cves:
                pkg_objset.new_relationship(
                    [spdx_package],
                    oe.spdx30.RelationshipType.hasAssociatedVulnerability,
                    sorted(list(all_cves)),
                )

            bb.debug(1, "Adding package files to SPDX for package %s" % pkg_name)
            package_files = add_package_files(
                d,
                pkg_objset,
                pkgdest / package,
                lambda file_counter: pkg_objset.new_spdxid("package", pkg_name, "file", str(file_counter)),
                # TODO: Can we know the purpose here?
                lambda filepath: [],
                ignore_top_level_dirs=['CONTROL', 'DEBIAN'],
                archive=None,
            )

            if package_files:
                pkg_objset.new_relationship(
                    [spdx_package],
                    oe.spdx30.RelationshipType.contains,
                    sorted(list(package_files)),
                )

            if include_sources:
                debug_sources = get_package_sources_from_debug(d, package, package_files, sources, source_hash_cache)
                debug_source_ids |= set(oe.sbom30.get_element_link_id(d) for d in debug_sources)

            oe.sbom30.write_recipe_jsonld_doc(d, pkg_objset, "packages-staging", deploydir, create_spdx_id_links=False)

    if include_sources:
        bb.debug(1, "Adding sysroot files to SPDX")
        sysroot_files = add_package_files(
            d,
            build_objset,
            d.expand("${COMPONENTS_DIR}/${PACKAGE_ARCH}/${PN}"),
            lambda file_counter: build_objset.new_spdxid("sysroot", str(file_counter)),
            lambda filepath: [],
            archive=None,
        )

        if sysroot_files:
            build_objset.new_scoped_relationship(
                [build],
                oe.spdx30.RelationshipType.hasOutputs,
                oe.spdx30.LifecycleScopeType.build,
                sorted(list(sysroot_files)),
            )

    if build_inputs or debug_source_ids:
        build_objset.new_scoped_relationship(
            [build],
            oe.spdx30.RelationshipType.hasInputs,
            oe.spdx30.LifecycleScopeType.build,
            sorted(list(build_inputs)) + sorted(list(debug_source_ids)),
        )

    oe.sbom30.write_recipe_jsonld_doc(d, build_objset, "recipes", deploydir)
}
do_create_spdx[vardepsexclude] += "BB_NUMBER_THREADS"
addtask do_create_spdx after \
    do_collect_spdx_deps \
    do_deploy_source_date_epoch \
    do_populate_sysroot do_package do_packagedata \
    ${create_spdx_source_deps(d)} \
    before do_populate_sdk do_populate_sdk_ext do_build do_rm_work

def create_spdx_source_deps(d):
    deps = []
    if d.getVar("SPDX_INCLUDE_SOURCES") == "1":
        deps.extend([
            # do_unpack is a hack for now; we only need it to get the
            # dependencies do_unpack already has so we can extract the source
            # ourselves
            "do_unpack",
            # For kernel source code
            "do_shared_workdir",
        ])
    return " ".join(deps)

SSTATETASKS += "do_create_spdx"
do_create_spdx[sstate-inputdirs] = "${SPDXDEPLOY}"
do_create_spdx[sstate-outputdirs] = "${DEPLOY_DIR_SPDX}"

python do_create_spdx_setscene () {
    sstate_setscene(d)
}
addtask do_create_spdx_setscene

do_create_spdx[dirs] = "${SPDXWORK}"
do_create_spdx[cleandirs] = "${SPDXDEPLOY} ${SPDXWORK}"
do_create_spdx[depends] += "${PATCHDEPENDENCY}"

python do_create_package_spdx() {
    import oe.sbom30
    import oe.spdx30
    import oe.spdx_common
    import oe.packagedata
    from pathlib import Path

    deploy_dir_spdx = Path(d.getVar("DEPLOY_DIR_SPDX"))
    deploydir = Path(d.getVar("SPDXRUNTIMEDEPLOY"))
    is_native = bb.data.inherits_class("native", d) or bb.data.inherits_class("cross", d)

    providers = oe.spdx_common.collect_package_providers(d)
    pkg_arch = d.getVar("SSTATE_PKGARCH")

    if not is_native:
        bb.build.exec_func("read_subpackage_metadata", d)

        dep_package_cache = {}

        # Any element common to all packages that need to be referenced by ID
        # should be written into this objset set
        common_objset = oe.sbom30.ObjectSet.new_objset(d, "%s-package-common" % d.getVar("PN"))

        pkgdest = Path(d.getVar("PKGDEST"))
        for package in d.getVar("PACKAGES").split():
            localdata = bb.data.createCopy(d)
            pkg_name = d.getVar("PKG:%s" % package) or package
            localdata.setVar("PKG", pkg_name)
            localdata.setVar('OVERRIDES', d.getVar("OVERRIDES", False) + ":" + package)

            if not oe.packagedata.packaged(package, localdata):
                continue

            spdx_package, pkg_objset = oe.sbom30.load_obj_in_jsonld(
                d,
                pkg_arch,
                "packages-staging",
                pkg_name,
                oe.spdx30.software_Package,
                software_primaryPurpose=oe.spdx30.software_SoftwarePurpose.install,
            )

            # We will write out a new collection, so link it to the new
            # creation info in the common package data. The old creation info
            # should still exist and be referenced by all the existing elements
            # in the package
            pkg_objset.creationInfo = pkg_objset.copy_creation_info(common_objset.doc.creationInfo)

            runtime_spdx_deps = set()

            deps = bb.utils.explode_dep_versions2(localdata.getVar("RDEPENDS") or "")
            seen_deps = set()
            for dep, _ in deps.items():
                if dep in seen_deps:
                    continue

                if dep not in providers:
                    continue

                (dep, _) = providers[dep]

                if not oe.packagedata.packaged(dep, localdata):
                    continue

                dep_pkg_data = oe.packagedata.read_subpkgdata_dict(dep, d)
                dep_pkg = dep_pkg_data["PKG"]

                if dep in dep_package_cache:
                    dep_spdx_package = dep_package_cache[dep]
                else:
                    bb.debug(1, "Searching for %s" % dep_pkg)
                    dep_spdx_package, _ = oe.sbom30.find_root_obj_in_jsonld(
                        d,
                        "packages-staging",
                        dep_pkg,
                        oe.spdx30.software_Package,
                        software_primaryPurpose=oe.spdx30.software_SoftwarePurpose.install,
                    )
                    dep_package_cache[dep] = dep_spdx_package

                runtime_spdx_deps.add(dep_spdx_package)
                seen_deps.add(dep)

            if runtime_spdx_deps:
                pkg_objset.new_scoped_relationship(
                    [spdx_package],
                    oe.spdx30.RelationshipType.dependsOn,
                    oe.spdx30.LifecycleScopeType.runtime,
                    [oe.sbom30.get_element_link_id(dep) for dep in runtime_spdx_deps],
                )

            oe.sbom30.write_recipe_jsonld_doc(d, pkg_objset, "packages", deploydir)

        oe.sbom30.write_recipe_jsonld_doc(d, common_objset, "common-package", deploydir)
}

do_create_package_spdx[vardepsexclude] += "OVERRIDES SSTATE_ARCHS"

addtask do_create_package_spdx after do_create_spdx before do_build do_rm_work
SSTATETASKS += "do_create_package_spdx"
do_create_package_spdx[sstate-inputdirs] = "${SPDXRUNTIMEDEPLOY}"
do_create_package_spdx[sstate-outputdirs] = "${DEPLOY_DIR_SPDX}"

python do_create_package_spdx_setscene () {
    sstate_setscene(d)
}
addtask do_create_package_spdx_setscene

do_create_package_spdx[dirs] = "${SPDXRUNTIMEDEPLOY}"
do_create_package_spdx[cleandirs] = "${SPDXRUNTIMEDEPLOY}"
do_create_package_spdx[rdeptask] = "do_create_spdx"



python spdx30_build_started_handler () {
    import oe.spdx30
    import oe.sbom30
    import oe.spdx_common
    import os
    from pathlib import Path
    from datetime import datetime, timezone

    # Create a copy of the datastore. Set PN to "bitbake" so that SPDX IDs can
    # be generated
    d = e.data.createCopy()
    d.setVar("PN", "bitbake")
    d.setVar("BB_TASKHASH", "bitbake")
    oe.spdx_common.load_spdx_license_data(d)

    deploy_dir_spdx = Path(e.data.getVar("DEPLOY_DIR_SPDX"))

    objset = oe.sbom30.ObjectSet.new_objset(d, "bitbake", False)

    host_import_key = d.getVar("SPDX_BUILD_HOST")
    invoked_by = objset.new_agent("SPDX_INVOKED_BY", add=False)
    on_behalf_of = objset.new_agent("SPDX_ON_BEHALF_OF", add=False)

    if d.getVar("SPDX_INCLUDE_BITBAKE_PARENT_BUILD") == "1":
        # Since the Build objects are unique, we may as well set the creation
        # time to the current time instead of the fallback SDE
        objset.doc.creationInfo.created = datetime.now(timezone.utc)

        # Each invocation of bitbake should have a unique ID since it is a
        # unique build
        nonce = os.urandom(16).hex()

        build = objset.add_root(oe.spdx30.build_Build(
            _id=objset.new_spdxid(nonce, include_unihash=False),
            creationInfo=objset.doc.creationInfo,
            build_buildType=oe.sbom30.SPDX_BUILD_TYPE,
        ))
        set_timestamp_now(d, build, "build_buildStartTime")

        if host_import_key:
            objset.new_scoped_relationship(
                [build],
                oe.spdx30.RelationshipType.hasHost,
                oe.spdx30.LifecycleScopeType.build,
                [objset.new_import("SPDX_BUILD_HOST")],
            )

        if invoked_by:
            objset.add(invoked_by)
            invoked_by_spdx = objset.new_scoped_relationship(
                [build],
                oe.spdx30.RelationshipType.invokedBy,
                oe.spdx30.LifecycleScopeType.build,
                [invoked_by],
            )

            if on_behalf_of:
                objset.add(on_behalf_of)
                objset.new_scoped_relationship(
                    [on_behalf_of],
                    oe.spdx30.RelationshipType.delegatedTo,
                    oe.spdx30.LifecycleScopeType.build,
                    invoked_by_spdx,
                )

        elif on_behalf_of:
            bb.warn("SPDX_ON_BEHALF_OF has no effect if SPDX_INVOKED_BY is not set")

    else:
        if host_import_key:
            bb.warn("SPDX_BUILD_HOST has no effect if SPDX_INCLUDE_BITBAKE_PARENT_BUILD is not set")

        if invoked_by:
            bb.warn("SPDX_INVOKED_BY has no effect if SPDX_INCLUDE_BITBAKE_PARENT_BUILD is not set")

        if on_behalf_of:
            bb.warn("SPDX_ON_BEHALF_OF has no effect if SPDX_INCLUDE_BITBAKE_PARENT_BUILD is not set")

    for obj in objset.foreach_type(oe.spdx30.Element):
        obj.extension.append(oe.sbom30.OELinkExtension(link_spdx_id=False))
        obj.extension.append(oe.sbom30.OEIdAliasExtension())

    oe.sbom30.write_jsonld_doc(d, objset, deploy_dir_spdx / "bitbake.spdx.json")
}

addhandler spdx30_build_started_handler
spdx30_build_started_handler[eventmask] = "bb.event.BuildStarted"

