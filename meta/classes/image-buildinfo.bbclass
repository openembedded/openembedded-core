#
# Writes build information to target filesystem on /etc/build
#
# Copyright (C) 2014 Intel Corporation
# Author: Alejandro Enedino Hernandez Samaniego <alejandro.hernandez@intel.com>
#
# Licensed under the MIT license, see COPYING.MIT for details
#
# Usage: add INHERIT += "image-buildinfo" to your conf file
#

# Desired variables to display 
IMAGE_BUILDINFO_VARS ?= "DISTRO DISTRO_VERSION"

# Desired location of the output file in the image.
IMAGE_BUILDINFO_FILE ??= "${sysconfdir}/build"

# From buildhistory.bbclass
def image_buildinfo_outputvars(vars, d):
    vars = vars.split()
    ret = ""
    for var in vars:
        value = d.getVar(var) or ""
        if (d.getVarFlag(var, 'type') == "list"):
            value = oe.utils.squashspaces(value)
        ret += "%s = %s\n" % (var, value)
    return ret.rstrip('\n')

# Returns layer revisions along with their respective status
def get_layer_revs(d):
    layers = (d.getVar("BBLAYERS") or "").split()
    medadata_revs = ["%-17s = %s:%s %s" % (os.path.basename(i), \
        oe.buildcfg.get_metadata_git_branch(i, None).strip(), \
        oe.buildcfg.get_metadata_git_revision(i, None), \
        oe.buildcfg.is_layer_modified(i)) \
            for i in layers]
    return '\n'.join(medadata_revs)

def buildinfo_target(d):
        # Get context
        if d.getVar('BB_WORKERCONTEXT') != '1':
                return ""
        # Single and list variables to be read
        vars = (d.getVar("IMAGE_BUILDINFO_VARS") or "")
        return image_buildinfo_outputvars(vars, d)

# Write build information to target filesystem
python buildinfo () {
    if not d.getVar('IMAGE_BUILDINFO_FILE'):
        return
    with open(d.expand('${IMAGE_ROOTFS}${IMAGE_BUILDINFO_FILE}'), 'w') as build:
        build.writelines((
            '''-----------------------
Build Configuration:  |
-----------------------
''',
            buildinfo_target(d),
            '''
-----------------------
Layer Revisions:      |
-----------------------
''',
            get_layer_revs(d),
            '''
'''
       ))
}

IMAGE_PREPROCESS_COMMAND += "buildinfo;"
