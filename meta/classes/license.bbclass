# Populates LICENSE_DIRECTORY as set in distro config with the license files as set by
# LIC_FILES_CHKSUM.
# TODO:
# - There is a real issue revolving around license naming standards.

LICENSE_DIRECTORY ??= "${DEPLOY_DIR}/licenses"
LICSSTATEDIR = "${WORKDIR}/license-destdir/"

addtask populate_lic after do_patch before do_package
do_populate_lic[dirs] = "${LICSSTATEDIR}/${PN}"
do_populate_lic[cleandirs] = "${LICSSTATEDIR}"

# Standards are great! Everyone has their own. In an effort to standardize licensing
# names, common-licenses will use the SPDX standard license names. In order to not
# break the non-standardized license names that we find in LICENSE, we'll set
# up a bunch of VarFlags to accomodate non-SPDX license names.
#
# We should really discuss standardizing this field, but that's a longer term goal.
# For now, we can do this and it should grab the most common LICENSE naming variations.
#
# Changing GPL mapping to GPL-2 as it's not very likely to be GPL-1
# We should NEVER have a GPL/LGPL without a version!!!!
# Any mapping to MPL/LGPL/GPL should be fixed
# see: https://wiki.yoctoproject.org/wiki/License_Audit

# GPL variations
SPDXLICENSEMAP[GPL-2] = "GPL-2.0"
SPDXLICENSEMAP[GPLv2] = "GPL-2.0"
SPDXLICENSEMAP[GPLv2.0] = "GPL-2.0"
SPDXLICENSEMAP[GPL-3] = "GPL-3.0"
SPDXLICENSEMAP[GPLv3] = "GPL-3.0"
SPDXLICENSEMAP[GPLv3.0] = "GPL-3.0"

#LGPL variations
SPDXLICENSEMAP[LGPLv2] = "LGPL-2.0"
SPDXLICENSEMAP[LGPL2.1] = "LGPL-2.1"
SPDXLICENSEMAP[LGPLv2.1] = "LGPL-2.1"
SPDXLICENSEMAP[LGPLv3] = "LGPL-3.0"

#MPL variations
SPDXLICENSEMAP[MPL-1] = "MPL-1.0"
SPDXLICENSEMAP[MPLv1] = "MPL-1.0"
SPDXLICENSEMAP[MPLv1.1] = "MPL-1.1"

#MIT variations
SPDXLICENSEMAP[MIT-X] = "MIT"
SPDXLICENSEMAP[MIT-style] = "MIT"

#Openssl variations
SPDXLICENSEMAP[openssl] = "OpenSSL"

#Python variations
SPDXLICENSEMAP[PSF] = "Python-2.0"
SPDXLICENSEMAP[PSFv2] = "Python-2.0"
SPDXLICENSEMAP[Python-2] = "Python-2.0"

#Apache variations
SPDXLICENSEMAP[Apachev2] = "Apache-2.0"
SPDXLICENSEMAP[Apache-2] = "Apache-2.0"

#Artistic variations
SPDXLICENSEMAP[Artisticv1] = "Artistic-1.0"
SPDXLICENSEMAP[Artistic-1] = "Artistic-1.0"

#Academic variations
SPDXLICENSEMAP[AFL-2] = "AFL-2.0"
SPDXLICENSEMAP[AFL-1] = "AFL-1.2"
SPDXLICENSEMAP[AFLv2] = "AFL-2.0"
SPDXLICENSEMAP[AFLv1] = "AFL-1.2"

#Other variations
SPDXLICENSEMAP[EPLv1.0] = "EPL-1.0"

license_create_manifest() {
    mkdir -p ${LICENSE_DIRECTORY}/${IMAGE_NAME}
    # Get list of installed packages
    list_installed_packages | grep -v "locale" |sort > ${LICENSE_DIRECTORY}/${IMAGE_NAME}/package.manifest
    INSTALLED_PKGS=`cat ${LICENSE_DIRECTORY}/${IMAGE_NAME}/package.manifest`
    # list of installed packages is broken for deb
    for pkg in ${INSTALLED_PKGS}; do
        # not the best way to do this but licenses are not arch dependant iirc
        files=`find ${TMPDIR}/pkgdata/*/runtime -name ${pkg}| head -1`
        for filename in $files; do
            pkged_pn="$(sed -n 's/^PN: //p' ${filename})"
            pkged_lic="$(sed -n '/^LICENSE: /{ s/^LICENSE: //; s/[+|&()*]/ /g; s/  */ /g; p }' ${filename})"
            # check to see if the package name exists in the manifest. if so, bail.
            if ! grep -q "PACKAGE NAME: ${pkg}" ${filename}; then
                # exclude local recipes
                if [ ! "${pkged_pn}" = "*locale*" ]; then
                    echo "PACKAGE NAME:" ${pkg} >> ${LICENSE_DIRECTORY}/${IMAGE_NAME}/license.manifest
                    echo "RECIPE NAME:" ${pkged_pn} >> ${LICENSE_DIRECTORY}/${IMAGE_NAME}/license.manifest
                    echo "LICENSE: " >> ${LICENSE_DIRECTORY}/${IMAGE_NAME}/license.manifest
                    for lic in ${pkged_lic}; do
                        if [ -e "${LICENSE_DIRECTORY}/${pkged_pn}/generic_${lic}" ]; then
                            echo ${lic}|sed s'/generic_//'g >> ${LICENSE_DIRECTORY}/${IMAGE_NAME}/license.manifest
                        else
                            echo "WARNING: The license listed, " ${lic} " was not in the licenses collected for " ${pkged_pn}>> ${LICENSE_DIRECTORY}/${IMAGE_NAME}/license.manifest
                        fi
                    done
                    echo "" >> ${LICENSE_DIRECTORY}/${IMAGE_NAME}/license.manifest
                fi
            fi
        done
    done

    # Two options here:
    # - Just copy the manifest
    # - Copy the manifest and the license directories
    # This will make your image a bit larger, however 
    # if you are concerned about license compliance 
    # and delivery this should cover all your bases

    if [ -n ${COPY_LIC_MANIFEST} ]; then
        mkdir -p ${IMAGE_ROOTFS}/usr/share/common-licenses/
        cp ${LICENSE_DIRECTORY}/${IMAGE_NAME}/license.manifest ${IMAGE_ROOTFS}/usr/share/common-licenses/license.manifest
        if [ -n ${COPY_LIC_DIRS} ]; then
            for pkg in ${INSTALLED_PKGS}; do
                mkdir -p ${IMAGE_ROOTFS}/usr/share/common-licenses/${pkg}
                for lic in `ls ${LICENSE_DIRECTORY}/${pkged_pn}`; do
                    # Really don't need to copy the generics as they're 
                    # represented in the manifest and in the actual pkg licenses
                    # Doing so would make your image quite a bit larger
                    if [ ! ${lic} == "generic_*" ]; then
                        cp ${LICENSE_DIRECTORY}/${pkged_pn}/${lic} ${IMAGE_ROOTFS}/usr/share/common-licenses/${pkg}/${lic}
                    fi
                done
            done
        fi
    fi

}


python do_populate_lic() {
    """
    Populate LICENSE_DIRECTORY with licenses.
    """
    import os
    import bb
    import shutil
    import oe.license

    # All the license types for the package
    license_types = d.getVar('LICENSE', True)
    # All the license files for the package
    lic_files = d.getVar('LIC_FILES_CHKSUM', True)
    pn = d.getVar('PN', True)
    # The base directory we wrangle licenses to
    destdir = os.path.join(d.getVar('LICSSTATEDIR', True), pn)
    # The license files are located in S/LIC_FILE_CHECKSUM.
    srcdir = d.getVar('S', True)
    # Directory we store the generic licenses as set in the distro configuration
    generic_directory = d.getVar('COMMON_LICENSE_DIR', True)
    license_source_dirs = []
    license_source_dirs.append(generic_directory)
    try:
        additional_lic_dirs = d.getVar('LICENSE_DIR', True).split()
        for lic_dir in additional_lic_dirs:
            license_source_dirs.append(lic_dir)
    except:
        pass

    class FindVisitor(oe.license.LicenseVisitor):
        def visit_Str(self, node):
            #
            # Until I figure out what to do with
            # the two modifiers I support (or greater = +
            # and "with exceptions" being *
            # we'll just strip out the modifier and put
            # the base license.
            find_license(node.s.replace("+", "").replace("*", ""))
            self.generic_visit(node)

    def find_license(license_type):
        try:
            bb.mkdirhier(gen_lic_dest)
        except:
            pass
        spdx_generic = None
        license_source = None
        # If the generic does not exist we need to check to see if there is an SPDX mapping to it
        for lic_dir in license_source_dirs:
            if not os.path.isfile(os.path.join(lic_dir, license_type)):
                if d.getVarFlag('SPDXLICENSEMAP', license_type) != None:
                    # Great, there is an SPDXLICENSEMAP. We can copy!
                    bb.debug(1, "We need to use a SPDXLICENSEMAP for %s" % (license_type))
                    spdx_generic = d.getVarFlag('SPDXLICENSEMAP', license_type)
                    license_source = lic_dir
                    break
            elif os.path.isfile(os.path.join(lic_dir, license_type)):
                spdx_generic = license_type
                license_source = lic_dir
                break

        if spdx_generic and license_source:
            # we really should copy to generic_ + spdx_generic, however, that ends up messing the manifest
            # audit up. This should be fixed in emit_pkgdata (or, we actually got and fix all the recipes)
            ret = bb.copyfile(os.path.join(license_source, spdx_generic), os.path.join(os.path.join(d.getVar('LICSSTATEDIR', True), pn), "generic_" + license_type))
            # If the copy didn't occur, something horrible went wrong and we fail out
            if not ret:
                bb.warn("%s for %s could not be copied for some reason. It may not exist. WARN for now." % (spdx_generic, pn))
        else:
            # And here is where we warn people that their licenses are lousy
            bb.warn("%s: No generic license file exists for: %s in any provider" % (pn, license_type))
            pass

    try:
        bb.mkdirhier(destdir)
    except:
        pass

    if not generic_directory:
        raise bb.build.FuncFailed("COMMON_LICENSE_DIR is unset. Please set this in your distro config")

    if not lic_files:
        # No recipe should have an invalid license file. This is checked else
        # where, but let's be pedantic
        bb.note(pn + ": Recipe file does not have license file information.")
        return True

    for url in lic_files.split():
        (type, host, path, user, pswd, parm) = bb.decodeurl(url)
        # We want the license file to be copied into the destination
        srclicfile = os.path.join(srcdir, path)
        ret = bb.copyfile(srclicfile, os.path.join(destdir, os.path.basename(path)))
        # If the copy didn't occur, something horrible went wrong and we fail out
        if not ret:
            bb.warn("%s could not be copied for some reason. It may not exist. WARN for now." % srclicfile)

    v = FindVisitor()
    try:
        v.visit_string(license_types)
    except oe.license.InvalidLicense as exc:
        bb.fatal('%s: %s' % (d.getVar('PF', True), exc))
    except SyntaxError:
        bb.warn("%s: Failed to parse it's LICENSE field." % (d.getVar('PF', True)))

}

SSTATETASKS += "do_populate_lic"
do_populate_lic[sstate-name] = "populate-lic"
do_populate_lic[sstate-inputdirs] = "${LICSSTATEDIR}"
do_populate_lic[sstate-outputdirs] = "${LICENSE_DIRECTORY}/"

ROOTFS_POSTINSTALL_COMMAND += "license_create_manifest; "

python do_populate_lic_setscene () {
	sstate_setscene(d)
}
addtask do_populate_lic_setscene
