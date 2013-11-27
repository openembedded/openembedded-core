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

# Find and dump layer info when we got the layers parsed



python toaster_layerinfo_dumpdata() {
    import subprocess

    def _get_git_branch(layer_path):
        branch = subprocess.Popen("git symbolic-ref HEAD 2>/dev/null ", cwd=layer_path, shell=True, stdout=subprocess.PIPE).communicate()[0]
        branch = branch.replace('refs/heads/', '').rstrip()
        return branch

    def _get_git_revision(layer_path):
        revision = subprocess.Popen("git rev-parse HEAD 2>/dev/null ", cwd=layer_path, shell=True, stdout=subprocess.PIPE).communicate()[0].rstrip()
        return revision

    def _get_url_map_name(layer_name):
        """ Some layers have a different name on openembedded.org site,
            this method returns the correct name to use in the URL
        """

        url_name = layer_name
        url_mapping = {'meta': 'openembedded-core'}

        for key in url_mapping.keys():
            if key == layer_name:
                url_name = url_mapping[key]

        return url_name

    def _get_layer_version_information(layer_path):

        layer_version_info = {}
        layer_version_info['branch'] = _get_git_branch(layer_path)
        layer_version_info['commit'] = _get_git_revision(layer_path)
        layer_version_info['priority'] = 0

        return layer_version_info


    def _get_layer_dict(layer_path):

        layer_info = {}
        layer_name = layer_path.split('/')[-1]
        layer_url = 'http://layers.openembedded.org/layerindex/layer/{layer}/'
        layer_url_name = _get_url_map_name(layer_name)

        layer_info['name'] = layer_name
        layer_info['local_path'] = layer_path
        layer_info['layer_index_url'] = layer_url.format(layer=layer_url_name)
        layer_info['version'] = _get_layer_version_information(layer_path)

        return layer_info


    bblayers = e.data.getVar("BBLAYERS", True)

    llayerinfo = {}

    for layer in { l for l in bblayers.strip().split(" ") if len(l) }:
        llayerinfo[layer] = _get_layer_dict(layer)


    bb.event.fire(bb.event.MetadataEvent("LayerInfo", llayerinfo), e.data)
}

# Dump package file info data

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



# collect list of buildstats files based on fired events; when the build completes, collect all stats and fire an event with collected data

python toaster_collect_task_stats() {
    import bb.build
    import bb.event
    import bb.data
    import bb.utils
    import os

    def _append_read_list(v):
        lock = bb.utils.lockfile(e.data.expand("${TOPDIR}/toaster.lock"), False, True)

        with open(os.path.join(e.data.getVar('BUILDSTATS_BASE', True), "toasterstatlist"), "a") as fout:
            bn = get_bn(e)
            bsdir = os.path.join(e.data.getVar('BUILDSTATS_BASE', True), bn)
            taskdir = os.path.join(bsdir, e.data.expand("${PF}"))
            fout.write("%s:%s:%s\n" % (e.taskfile, e.taskname, os.path.join(taskdir, e.task)))

        bb.utils.unlockfile(lock)

    def _read_stats(filename):
        cpu_usage = 0
        disk_io = 0
        startio = ''
        endio = ''
        pn = ''
        taskname = ''
        statinfo = {}

        with open(filename, 'r') as task_bs:
            for line in task_bs.readlines():
                k,v = line.strip().split(": ", 1)
                statinfo[k] = v

        try:
            cpu_usage = statinfo["CPU usage"]
            endio = statinfo["EndTimeIO"]
            startio = statinfo["StartTimeIO"]
        except KeyError:
            pass    # we may have incomplete data here

        if startio and endio:
            disk_io = int(endio.strip('\n ')) - int(startio.strip('\n '))

        if cpu_usage:
            cpu_usage = float(cpu_usage.strip('% \n'))

        return {'cpu_usage': cpu_usage, 'disk_io': disk_io}


    if isinstance(e, (bb.build.TaskSucceeded, bb.build.TaskFailed)):
        _append_read_list(e)
        pass


    if isinstance(e, bb.event.BuildCompleted):
        events = []
        with open(os.path.join(e.data.getVar('BUILDSTATS_BASE', True), "toasterstatlist"), "r") as fin:
            for line in fin:
                (taskfile, taskname, filename) = line.strip().split(":")
                events.append((taskfile, taskname, _read_stats(filename)))
        bb.event.fire(bb.event.MetadataEvent("BuildStatsList", events), e.data)
        os.unlink(os.path.join(e.data.getVar('BUILDSTATS_BASE', True), "toasterstatlist"))
}


# set event handlers
addhandler toaster_layerinfo_dumpdata
toaster_layerinfo_dumpdata[eventmask] = "bb.event.TreeDataPreparationCompleted"

addhandler toaster_collect_task_stats
toaster_collect_task_stats[eventmask] = "bb.event.BuildCompleted bb.build.TaskSucceeded bb.build.TaskFailed"
do_package[postfuncs] += "toaster_package_dumpdata "

do_rootfs[postfuncs] += "toaster_image_dumpdata "
