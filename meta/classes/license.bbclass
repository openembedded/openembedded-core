# Populates LICENSE_DIRECTORY as set in poky.conf with the license files as set by
# LIC_FILES_CHKSUM. 
# TODO:
# - We should also enable the ability to put the generated license directory onto the
#  rootfs
# - Gather up more generic licenses
# - There is a real issue revolving around license naming standards. See license names 
#  licenses.conf and compare them to the license names in the recipes. You'll see some
#  differences and that should be corrected.

LICENSE_DIRECTORY ??= "${DEPLOY_DIR_IMAGE}/licenses"
LICSSTATEDIR = "${WORKDIR}/license-destdir/"

addtask populate_lic after do_patch before do_package 
do_populate_lic[dirs] = "${LICSSTATEDIR}/${PN}"
do_populate_lic[cleandirs] = "${LICSSTATEDIR}"
python do_populate_lic() {
    """
    Populate LICENSE_DIRECTORY with licenses.
    """
    import os
    import bb
    import shutil

    # All the license types for the package
    license_types = bb.data.getVar('LICENSE', d, True)
    # All the license files for the package
    lic_files = bb.data.getVar('LIC_FILES_CHKSUM', d, True)
    pn = bb.data.getVar('PN', d, True)
    # The base directory we wrangle licenses to
    destdir = os.path.join(bb.data.getVar('LICSSTATEDIR', d, True), pn)
    # The license files are located in S/LIC_FILE_CHECKSUM.
    srcdir = bb.data.getVar('S', d, True)
    # Directory we store the generic licenses as set in poky.conf
    generic_directory = bb.data.getVar('COMMON_LICENSE_DIR', d, True)
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
 
    # This takes some explaining.... we now are going to go an try to symlink 
    # to a generic file. But, with the way LICENSE works, a package can have multiple
    # licenses. Some of them are, for example, GPLv2+, which means it can use that version
    # of GPLv2 specified in it's license, or a later version of GPLv2. For the purposes of
    # what we're doing here, we really don't track license revisions (although we may want to)
    # So, we strip out the + and link to a generic GPLv2
    #
    # That said, there are some entries into LICENSE that either have no generic (bzip, zlib, ICS)
    # or the LICENSE is messy (Apache 2.0 .... when they mean Apache-2.0). This should be corrected
    # but it's outside of scope for this.
    #
    # Also, you get some clever license fields with logic in the field. 
    # I'm sure someone has written a logic parser for these fields, but if so, I don't know where it is. 
    # So what I do is just link to every license mentioned in the license field.
    
    for license_type in (' '.join(license_types.replace('&', ' ').replace('+', ' ').replace('|', ' ')
                         .replace('(', ' ').replace(')', ' ').replace(';', ' ').replace(',', ' ').split())):
        if os.path.isfile(os.path.join(generic_directory, license_type)):
            gen_lic_dest = os.path.join(bb.data.getVar('LICENSE_DIRECTORY', d, True), "common-licenses")
            try:
                bb.mkdirhier(gen_lic_dest)
            except:
                pass
            
            try:
                bb.copyfile(os.path.join(generic_directory, license_type), os.path.join(gen_lic_dest, license_type))
            except:
                bb.warn("%s: No generic license file exists for: %s at %s" % (pn, license_type, generic_directory))
                pass 
            try:
                os.symlink(os.path.join(gen_lic_dest, license_type), os.path.join(destdir, "generic_" + license_type))
            except:
                bb.warn("%s: No generic license file exists for: %s at %s" % (pn, license_type, generic_directory))
                pass
        else:
            bb.warn("%s: Something went wrong with copying: %s to %s" % (pn, license_type, generic_directory))
            bb.warn("This could be either because we do not have a generic for this license or the LICENSE field is incorrect")
            pass
}

SSTATETASKS += "do_populate_lic"
do_populate_lic[sstate-name] = "populate-lic"
do_populate_lic[sstate-inputdirs] = "${LICSSTATEDIR}"
do_populate_lic[sstate-outputdirs] = "${LICENSE_DIRECTORY}/"

python do_populate_lic_setscene () {
	sstate_setscene(d)
}
addtask do_populate_lic_setscene


