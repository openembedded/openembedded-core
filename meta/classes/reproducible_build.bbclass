# reproducible_build.bbclass
#
# Sets SOURCE_DATE_EPOCH in each component's build environment.
# Upstream components (generally) respect this environment variable,
# using it in place of the "current" date and time.
# See https://reproducible-builds.org/specs/source-date-epoch/
#
# After sources are unpacked but before they are patched, we set a reproducible value for SOURCE_DATE_EPOCH.
# This value should be reproducible for anyone who builds the same revision from the same sources.
#
# There are 4 ways we determine SOURCE_DATE_EPOCH:
#
# 1. Use the value from __source_date_epoch.txt file if this file exists.
#    This file was most likely created in the previous build by one of the following methods 2,3,4.
#    Alternatively, it can be provided by a recipe via SRC_URI.
#
# If the file does not exist:
#
# 2. If there is a git checkout, use the last git commit timestamp.
#    Git does not preserve file timestamps on checkout.
#
# 3. Use the mtime of "known" files such as NEWS, CHANGLELOG, ...
#    This works for well-kept repositories distributed via tarball.
#
# 4. If the above steps fail, use the modification time of the youngest file in the source tree.
#
# Once the value of SOURCE_DATE_EPOCH is determined, it is stored in the recipe's ${SDE_FILE}.
# If this file is found by other tasks, the value is exported in the SOURCE_DATE_EPOCH variable.
# SOURCE_DATE_EPOCH is set for all tasks that might use it (do_configure, do_compile, do_package, ...)

BUILD_REPRODUCIBLE_BINARIES ??= '1'
inherit ${@oe.utils.ifelse(d.getVar('BUILD_REPRODUCIBLE_BINARIES') == '1', 'reproducible_build_simple', '')}

SDE_DIR ="${WORKDIR}/source-date-epoch"
SDE_FILE = "${SDE_DIR}/__source_date_epoch.txt"

SSTATETASKS += "do_deploy_source_date_epoch"

do_deploy_source_date_epoch () {
    echo "Deploying SDE to ${SDE_DIR}."
}

python do_deploy_source_date_epoch_setscene () {
    sstate_setscene(d)
}

do_deploy_source_date_epoch[dirs] = "${SDE_DIR}"
do_deploy_source_date_epoch[sstate-plaindirs] = "${SDE_DIR}"
addtask do_deploy_source_date_epoch_setscene
addtask do_deploy_source_date_epoch before do_configure after do_patch

def get_source_date_epoch_known_files(d, path):
    source_date_epoch = 0
    known_files = set(["NEWS", "ChangeLog", "Changelog", "CHANGES"])
    for file in known_files:
        filepath = os.path.join(path,file)
        if os.path.isfile(filepath):
            mtime = int(os.lstat(filepath).st_mtime)
            # There may be more than one "known_file" present, if so, use the youngest one
            if mtime > source_date_epoch:
                source_date_epoch = mtime
    return source_date_epoch

def find_git_folder(path):
    exclude = set(["temp", "license-destdir", "patches", "recipe-sysroot-native", "recipe-sysroot", "pseudo", "build", "image", "sysroot-destdir"])
    for root, dirs, files in os.walk(path, topdown=True):
        dirs[:] = [d for d in dirs if d not in exclude]
        if '.git' in dirs:
            return root

def get_source_date_epoch_git(d, path):
    source_date_epoch = 0
    if "git://" in d.getVar('SRC_URI'):
        gitpath = find_git_folder(d.getVar('WORKDIR'))
        if gitpath != None:
            import subprocess
            if os.path.isdir(os.path.join(gitpath,".git")):
                try:
                    source_date_epoch = int(subprocess.check_output(['git','log','-1','--pretty=%ct'], cwd=path))
                    bb.debug(1, "git repo path:%s sde: %d" % (gitpath,source_date_epoch))
                except subprocess.CalledProcessError as grepexc:
                    bb.debug(1, "Expected git repository not found, (path: %s) error:%d" % (gitpath, grepexc.returncode))
        else:
            bb.warn("Failed to find a git repository for path:%s" % (path))
    return source_date_epoch

python do_create_source_date_epoch_stamp() {
    path = d.getVar('S')
    if not os.path.isdir(path):
        bb.warn("Unable to determine source_date_epoch! path:%s" % path)
        return

    epochfile = d.getVar('SDE_FILE')
    if os.path.isfile(epochfile):
        bb.debug(1, " path: %s reusing __source_date_epoch.txt" % epochfile)
        return

    source_date_epoch = get_source_date_epoch_git(d, path)
    if source_date_epoch == 0:
        source_date_epoch = get_source_date_epoch_known_files(d, path)
    if source_date_epoch == 0:
        # Do it the hard way: check all files and find the youngest one...
        filename_dbg = None
        exclude = set(["temp", "license-destdir", "patches", "recipe-sysroot-native", "recipe-sysroot", "pseudo", "build", "image", "sysroot-destdir"])
        for root, dirs, files in os.walk(path, topdown=True):
            files = [f for f in files if not f[0] == '.']
            dirs[:] = [d for d in dirs if d not in exclude]

            for fname in files:
                filename = os.path.join(root, fname)
                try:
                    mtime = int(os.lstat(filename).st_mtime)
                except ValueError:
                    mtime = 0
                if mtime > source_date_epoch:
                    source_date_epoch = mtime
                    filename_dbg = filename

        if filename_dbg != None:
            bb.debug(1," SOURCE_DATE_EPOCH %d derived from: %s" % (source_date_epoch, filename_dbg))

        if source_date_epoch == 0:
            # empty folder, not a single file ...
            # kernel source do_unpack is special cased
            if not bb.data.inherits_class('kernel', d):
                bb.debug(1, "Unable to determine source_date_epoch! path:%s" % path)

    bb.utils.mkdirhier(d.getVar('SDE_DIR'))
    with open(epochfile, 'w') as f:
        f.write(str(source_date_epoch))
}

BB_HASHBASE_WHITELIST += "SOURCE_DATE_EPOCH"

python () {
    if d.getVar('BUILD_REPRODUCIBLE_BINARIES') == '1':
        d.appendVarFlag("do_unpack", "postfuncs", " do_create_source_date_epoch_stamp")
        epochfile = d.getVar('SDE_FILE')
        source_date_epoch = "0"
        if os.path.isfile(epochfile):
            with open(epochfile, 'r') as f:
                source_date_epoch = f.read()
            bb.debug(1, "source_date_epoch stamp found ---> stamp %s" % source_date_epoch)
        d.setVar('SOURCE_DATE_EPOCH', source_date_epoch)
}
