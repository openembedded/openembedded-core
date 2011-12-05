# Populates LICENSE_DIRECTORY as set in distro config with the license files as set by
# LIC_FILES_CHKSUM.
# TODO:
# - We should also enable the ability to put the generated license directory onto the
#  rootfs
# - Gather up more generic licenses
# - There is a real issue revolving around license naming standards. See license names
#  licenses.conf and compare them to the license names in the recipes. You'll see some
#  differences and that should be corrected.

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

#GPL variations
SPDXLICENSEMAP[GPL] = "GPL-1"
SPDXLICENSEMAP[GPLv2] = "GPL-2"
SPDXLICENSEMAP[GPLv3] = "GPL-3"

#LGPL variations
SPDXLICENSEMAP[LGPL] = "LGPL-2"
SPDXLICENSEMAP[LGPLv2] = "LGPL-2"
SPDXLICENSEMAP[LGPL2.1] = "LGPL-2.1"
SPDXLICENSEMAP[LGPLv2.1] = "LGPL-2.1"
SPDXLICENSEMAP[LGPLv3] = "LGPL-3"

#MPL variations
SPDXLICENSEMAP[MPL] = "MPL-1"
SPDXLICENSEMAP[MPLv1] = "MPL-1"
SPDXLICENSEMAP[MPLv1.1] = "MPL-1"

#MIT variations
SPDXLICENSEMAP[MIT-X] = "MIT"

#Openssl variations
SPDXLICENSEMAP[openssl] = "OpenSSL"

#Other variations
SPDXLICENSEMAP[AFL2.1] = "AFL-2"
SPDXLICENSEMAP[EPLv1.0] = "EPL-1"

python do_populate_lic() {
    """
    Populate LICENSE_DIRECTORY with licenses.
    """
    import os
    import bb
    import shutil
    import oe.license

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

    def copy_license(source, destination, file_name):
        try:
            bb.copyfile(os.path.join(source, file_name), os.path.join(destination, file_name))
        except:
            bb.warn("%s: No generic license file exists for: %s at %s" % (pn, file_name, source))
            pass

    def link_license(source, destination, file_name):
        try:
            os.symlink(os.path.join(source, file_name), os.path.join(destination, "generic_" + file_name))
        except:
            bb.warn("%s: Could not symlink: %s at %s to %s at %s" % (pn, file_name, source, file_name, destination))
            pass

    def find_license(license_type):
        try:
            bb.mkdirhier(gen_lic_dest)
        except:
            pass

        # If the generic does not exist we need to check to see if there is an SPDX mapping to it
        if not os.path.isfile(os.path.join(generic_directory, license_type)):
            if d.getVarFlag('SPDXLICENSEMAP', license_type) != None:
                # Great, there is an SPDXLICENSEMAP. We can copy!
                bb.note("We need to use a SPDXLICENSEMAP for %s" % (license_type))
                spdx_generic = d.getVarFlag('SPDXLICENSEMAP', license_type)
                copy_license(generic_directory, gen_lic_dest, spdx_generic)
                link_license(gen_lic_dest, destdir, spdx_generic)
            else:
                # And here is where we warn people that their licenses are lousy
                bb.warn("%s: No generic license file exists for: %s at %s" % (pn, license_type, generic_directory))
                bb.warn("%s: There is also no SPDXLICENSEMAP for this license type: %s at %s" % (pn, license_type, generic_directory))
                pass
        elif os.path.isfile(os.path.join(generic_directory, license_type)):
            copy_license(generic_directory, gen_lic_dest, license_type)
            link_license(gen_lic_dest, destdir, license_type)

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
        if ret is False or ret == 0:
            bb.warn("%s could not be copied for some reason. It may not exist. WARN for now." % srclicfile)

    gen_lic_dest = os.path.join(d.getVar('LICENSE_DIRECTORY', True), "common-licenses")

    v = FindVisitor()
    try:
        v.visit_string(license_types)
    except oe.license.InvalidLicense as exc:
        bb.fatal("%s: %s" % (d.getVar('PF', True), exc))
}

SSTATETASKS += "do_populate_lic"
do_populate_lic[sstate-name] = "populate-lic"
do_populate_lic[sstate-inputdirs] = "${LICSSTATEDIR}"
do_populate_lic[sstate-outputdirs] = "${LICENSE_DIRECTORY}/"

python do_populate_lic_setscene () {
	sstate_setscene(d)
}
addtask do_populate_lic_setscene

