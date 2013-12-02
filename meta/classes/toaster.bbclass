#
# Toaster helper class
#
# Copyright (C) 2013 Intel Corporation
#
# Released under the MIT license (see COPYING.MIT)
#
# This bbclass is designed to extract data used by OE-Core during the build process,
# for recording in the Toaster system.
# The data access is synchronous, preserving the build data integrity across
# different builds.
#
# The data is transferred through the event system, using the MetadataEvent objects.
#
# The model is to enable the datadump functions as postfuncs, and have the dump
# executed after the real taskfunc has been executed. This prevents task signature changing
# is toaster is enabled or not. Build performance is not affected if Toaster is not enabled.
#
# To enable, use INHERIT in local.conf:
#
#       INHERIT += "toaster"
#
#
#
#

# 1. Dump package file info data

python toaster_package_dumpdata() {
    """
    Dumps the data created by emit_pkgdata
    """
    # replicate variables from the package.bbclass

    packages = d.getVar('PACKAGES', True)
    pkgdest = d.getVar('PKGDEST', True)

    pkgdatadir = d.getVar('PKGDESTWORK', True)


    # scan and send data for each package
    import json

    lpkgdata = {}
    for pkg in packages.split():

        subdata_file = pkgdatadir + "/runtime/%s" % pkg
        lpkgdata = {}

        sf = open(subdata_file, "r")
        line = sf.readline()
        while line:
            (n, v) = line.rstrip().split(":", 1)
            if pkg in n:
                n = n.replace("_" + pkg, "")
            if n == 'FILES_INFO':
                lpkgdata[n] = json.loads(v)
            else:
                lpkgdata[n] = v.strip()
            line = sf.readline()

        # Fire an event containing the pkg data
        bb.event.fire(bb.event.MetadataEvent("SinglePackageInfo", lpkgdata), d)
}

# 2. Dump output image files information

python toaster_image_dumpdata() {
    """
    Image filename for output images is not standardized.
    image_types.bbclass will spell out IMAGE_CMD_xxx variables that actually
    have hardcoded ways to create image file names in them.
    So we look for files starting with the set name.
    """

    deploy_dir_image = d.getVar('DEPLOY_DIR_IMAGE', True);
    image_name = d.getVar('IMAGE_NAME', True);

    image_info_data = {}

    for dirpath, dirnames, filenames in os.walk(deploy_dir_image):
        for fn in filenames:
            if fn.startswith(image_name):
                image_info_data[dirpath + fn] = os.stat(os.path.join(dirpath, fn)).st_size

    bb.event.fire(bb.event.MetadataEvent("ImageFileSize",image_info_data), d)
}


do_package[postfuncs] += "toaster_package_dumpdata "

do_rootfs[postfuncs] += "toaster_image_dumpdata "
