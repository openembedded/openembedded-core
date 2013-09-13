#
# Records history of build output in order to detect regressions
#
# Based in part on testlab.bbclass and packagehistory.bbclass
#
# Copyright (C) 2013 Intel Corporation
# Copyright (C) 2007-2011 Koen Kooi <koen@openembedded.org>
#

BUILDHISTORY_FEATURES ?= "image package sdk"
BUILDHISTORY_DIR ?= "${TOPDIR}/buildhistory"
BUILDHISTORY_DIR_IMAGE = "${BUILDHISTORY_DIR}/images/${MACHINE_ARCH}/${TCLIBC}/${IMAGE_BASENAME}"
BUILDHISTORY_DIR_PACKAGE = "${BUILDHISTORY_DIR}/packages/${MULTIMACH_TARGET_SYS}/${PN}"
BUILDHISTORY_DIR_SDK = "${BUILDHISTORY_DIR}/sdk/${SDK_NAME}/${IMAGE_BASENAME}"
BUILDHISTORY_IMAGE_FILES ?= "/etc/passwd /etc/group"
BUILDHISTORY_COMMIT ?= "0"
BUILDHISTORY_COMMIT_AUTHOR ?= "buildhistory <buildhistory@${DISTRO}>"
BUILDHISTORY_PUSH_REPO ?= ""

# Must inherit package first before changing PACKAGEFUNCS
inherit package
PACKAGEFUNCS += "buildhistory_emit_pkghistory"

# We don't want to force a rerun of do_package for everything
# if the buildhistory_emit_pkghistory function or any of the
# variables it refers to changes
do_package[vardepsexclude] += "buildhistory_emit_pkghistory"

#
# Called during do_package to write out metadata about this package
# for comparision when writing future packages
#
python buildhistory_emit_pkghistory() {
    import re

    if not "package" in (d.getVar('BUILDHISTORY_FEATURES', True) or "").split():
        return 0

    pkghistdir = d.getVar('BUILDHISTORY_DIR_PACKAGE', True)

    class RecipeInfo:
        def __init__(self, name):
            self.name = name
            self.pe = "0"
            self.pv = "0"
            self.pr = "r0"
            self.depends = ""
            self.packages = ""
            self.bbfile = ""
            self.src_uri = ""
            self.srcrev = ""
            self.srcrev_autorev = ""


    class PackageInfo:
        def __init__(self, name):
            self.name = name
            self.pe = "0"
            self.pv = "0"
            self.pr = "r0"
            # pkg/pkge/pkgv/pkgr should be empty because we want to be able to default them
            self.pkg = ""
            self.pkge = ""
            self.pkgv = ""
            self.pkgr = ""
            self.size = 0
            self.depends = ""
            self.rprovides = ""
            self.rdepends = ""
            self.rrecommends = ""
            self.rsuggests = ""
            self.rreplaces = ""
            self.rconflicts = ""
            self.files = ""
            self.filelist = ""
            # Variables that need to be written to their own separate file
            self.filevars = dict.fromkeys(['pkg_preinst', 'pkg_postinst', 'pkg_prerm', 'pkg_postrm'])

    # Should check PACKAGES here to see if anything removed

    def getpkgvar(pkg, var):
        val = bb.data.getVar('%s_%s' % (var, pkg), d, 1)
        if val:
            return val
        val = bb.data.getVar('%s' % (var), d, 1)

        return val

    def readPackageInfo(pkg, histfile):
        pkginfo = PackageInfo(pkg)
        with open(histfile, "r") as f:
            for line in f:
                lns = line.split('=')
                name = lns[0].strip()
                value = lns[1].strip(" \t\r\n").strip('"')
                if name == "PE":
                    pkginfo.pe = value
                elif name == "PV":
                    pkginfo.pv = value
                elif name == "PR":
                    pkginfo.pr = value
                elif name == "PKG":
                    pkginfo.pkg = value
                elif name == "PKGE":
                    pkginfo.pkge = value
                elif name == "PKGV":
                    pkginfo.pkgv = value
                elif name == "PKGR":
                    pkginfo.pkgr = value
                elif name == "RPROVIDES":
                    pkginfo.rprovides = value
                elif name == "RDEPENDS":
                    pkginfo.rdepends = value
                elif name == "RRECOMMENDS":
                    pkginfo.rrecommends = value
                elif name == "RSUGGESTS":
                    pkginfo.rsuggests = value
                elif name == "RREPLACES":
                    pkginfo.rreplaces = value
                elif name == "RCONFLICTS":
                    pkginfo.rconflicts = value
                elif name == "PKGSIZE":
                    pkginfo.size = long(value)
                elif name == "FILES":
                    pkginfo.files = value
                elif name == "FILELIST":
                    pkginfo.filelist = value
        # Apply defaults
        if not pkginfo.pkg:
            pkginfo.pkg = pkginfo.name
        if not pkginfo.pkge:
            pkginfo.pkge = pkginfo.pe
        if not pkginfo.pkgv:
            pkginfo.pkgv = pkginfo.pv
        if not pkginfo.pkgr:
            pkginfo.pkgr = pkginfo.pr
        return pkginfo

    def getlastpkgversion(pkg):
        try:
            histfile = os.path.join(pkghistdir, pkg, "latest")
            return readPackageInfo(pkg, histfile)
        except EnvironmentError:
            return None

    def sortpkglist(string):
        pkgiter = re.finditer(r'[a-zA-Z0-9.+-]+( \([><=]+ [^ )]+\))?', string, 0)
        pkglist = [p.group(0) for p in pkgiter]
        pkglist.sort()
        return ' '.join(pkglist)

    def sortlist(string):
        items = string.split(' ')
        items.sort()
        return ' '.join(items)

    pn = d.getVar('PN', True)
    pe = d.getVar('PE', True) or "0"
    pv = d.getVar('PV', True)
    pr = d.getVar('PR', True)

    bbfile = d.getVar('BB_FILENAME', True)
    src_uri = d.getVar('SRC_URI', True)
    srcrev = d.getVar('SRCREV', True)
    srcrev_autorev = 'yes' if d.getVar('SRCREV', False) == 'AUTOINC' else 'no'

    packages = squashspaces(d.getVar('PACKAGES', True))

    packagelist = packages.split()
    if not os.path.exists(pkghistdir):
        bb.utils.mkdirhier(pkghistdir)
    else:
        # Remove files for packages that no longer exist
        for item in os.listdir(pkghistdir):
            if item != "latest" and item != "latest_srcrev":
                if item not in packagelist:
                    subdir = os.path.join(pkghistdir, item)
                    for subfile in os.listdir(subdir):
                        os.unlink(os.path.join(subdir, subfile))
                    os.rmdir(subdir)

    rcpinfo = RecipeInfo(pn)
    rcpinfo.pe = pe
    rcpinfo.pv = pv
    rcpinfo.pr = pr
    rcpinfo.depends = sortlist(squashspaces(d.getVar('DEPENDS', True) or ""))
    rcpinfo.bbfile = bbfile
    rcpinfo.src_uri = src_uri
    rcpinfo.srcrev = srcrev
    rcpinfo.srcrev_autorev = srcrev_autorev
    rcpinfo.packages = packages
    write_recipehistory(rcpinfo, d)

    pkgdest = d.getVar('PKGDEST', True)
    for pkg in packagelist:
        pkge = getpkgvar(pkg, 'PKGE') or "0"
        pkgv = getpkgvar(pkg, 'PKGV')
        pkgr = getpkgvar(pkg, 'PKGR')
        #
        # Find out what the last version was
        # Make sure the version did not decrease
        #
        lastversion = getlastpkgversion(pkg)
        if lastversion:
            last_pkge = lastversion.pkge
            last_pkgv = lastversion.pkgv
            last_pkgr = lastversion.pkgr
            r = bb.utils.vercmp((pkge, pkgv, pkgr), (last_pkge, last_pkgv, last_pkgr))
            if r < 0:
                msg = "Package version for package %s went backwards which would break package feeds from (%s:%s-%s to %s:%s-%s)" % (pkg, last_pkge, last_pkgv, last_pkgr, pkge, pkgv, pkgr)
                package_qa_handle_error("version-going-backwards", msg, d)

        pkginfo = PackageInfo(pkg)
        # Apparently the version can be different on a per-package basis (see Python)
        pkginfo.pe = getpkgvar(pkg, 'PE') or "0"
        pkginfo.pv = getpkgvar(pkg, 'PV')
        pkginfo.pr = getpkgvar(pkg, 'PR')
        pkginfo.pkg = getpkgvar(pkg, 'PKG') or pkg
        pkginfo.pkge = pkge
        pkginfo.pkgv = pkgv
        pkginfo.pkgr = pkgr
        pkginfo.rprovides = sortpkglist(squashspaces(getpkgvar(pkg, 'RPROVIDES') or ""))
        pkginfo.rdepends = sortpkglist(squashspaces(getpkgvar(pkg, 'RDEPENDS') or ""))
        pkginfo.rrecommends = sortpkglist(squashspaces(getpkgvar(pkg, 'RRECOMMENDS') or ""))
        pkginfo.rsuggests = sortpkglist(squashspaces(getpkgvar(pkg, 'RSUGGESTS') or ""))
        pkginfo.rreplaces = sortpkglist(squashspaces(getpkgvar(pkg, 'RREPLACES') or ""))
        pkginfo.rconflicts = sortpkglist(squashspaces(getpkgvar(pkg, 'RCONFLICTS') or ""))
        pkginfo.files = squashspaces(getpkgvar(pkg, 'FILES') or "")
        for filevar in pkginfo.filevars:
            pkginfo.filevars[filevar] = getpkgvar(pkg, filevar)

        # Gather information about packaged files
        pkgdestpkg = os.path.join(pkgdest, pkg)
        filelist = []
        pkginfo.size = 0
        for f in pkgfiles[pkg]:
            relpth = os.path.relpath(f, pkgdestpkg)
            fstat = os.lstat(f)
            pkginfo.size += fstat.st_size
            filelist.append(os.sep + relpth)
        filelist.sort()
        pkginfo.filelist = " ".join(filelist)

        write_pkghistory(pkginfo, d)
}


def write_recipehistory(rcpinfo, d):
    bb.debug(2, "Writing recipe history")

    pkghistdir = d.getVar('BUILDHISTORY_DIR_PACKAGE', True)

    infofile = os.path.join(pkghistdir, "latest")
    with open(infofile, "w") as f:
        if rcpinfo.pe != "0":
            f.write("PE = %s\n" %  rcpinfo.pe)
        f.write("PV = %s\n" %  rcpinfo.pv)
        f.write("PR = %s\n" %  rcpinfo.pr)
        f.write("DEPENDS = %s\n" %  rcpinfo.depends)
        f.write("PACKAGES = %s\n" %  rcpinfo.packages)


def write_pkghistory(pkginfo, d):
    bb.debug(2, "Writing package history for package %s" % pkginfo.name)

    pkghistdir = d.getVar('BUILDHISTORY_DIR_PACKAGE', True)

    pkgpath = os.path.join(pkghistdir, pkginfo.name)
    if not os.path.exists(pkgpath):
        bb.utils.mkdirhier(pkgpath)

    infofile = os.path.join(pkgpath, "latest")
    with open(infofile, "w") as f:
        if pkginfo.pe != "0":
            f.write("PE = %s\n" %  pkginfo.pe)
        f.write("PV = %s\n" %  pkginfo.pv)
        f.write("PR = %s\n" %  pkginfo.pr)

        pkgvars = {}
        pkgvars['PKG'] = pkginfo.pkg if pkginfo.pkg != pkginfo.name else ''
        pkgvars['PKGE'] = pkginfo.pkge if pkginfo.pkge != pkginfo.pe else ''
        pkgvars['PKGV'] = pkginfo.pkgv if pkginfo.pkgv != pkginfo.pv else ''
        pkgvars['PKGR'] = pkginfo.pkgr if pkginfo.pkgr != pkginfo.pr else ''
        for pkgvar in pkgvars:
            val = pkgvars[pkgvar]
            if val:
                f.write("%s = %s\n" % (pkgvar, val))

        f.write("RPROVIDES = %s\n" %  pkginfo.rprovides)
        f.write("RDEPENDS = %s\n" %  pkginfo.rdepends)
        f.write("RRECOMMENDS = %s\n" %  pkginfo.rrecommends)
        if pkginfo.rsuggests:
            f.write("RSUGGESTS = %s\n" %  pkginfo.rsuggests)
        if pkginfo.rreplaces:
            f.write("RREPLACES = %s\n" %  pkginfo.rreplaces)
        if pkginfo.rconflicts:
            f.write("RCONFLICTS = %s\n" %  pkginfo.rconflicts)
        f.write("PKGSIZE = %d\n" %  pkginfo.size)
        f.write("FILES = %s\n" %  pkginfo.files)
        f.write("FILELIST = %s\n" %  pkginfo.filelist)

    for filevar in pkginfo.filevars:
        filevarpath = os.path.join(pkgpath, "latest.%s" % filevar)
        val = pkginfo.filevars[filevar]
        if val:
            with open(filevarpath, "w") as f:
                f.write(val)
        else:
            if os.path.exists(filevarpath):
                os.unlink(filevarpath)


buildhistory_get_installed() {
	mkdir -p $1

	# Get list of installed packages
	pkgcache="$1/installed-packages.tmp"
	list_installed_packages file | sort > $pkgcache

	cat $pkgcache | awk '{ print $1 }' > $1/installed-package-names.txt
	if [ -s $pkgcache ] ; then
		cat $pkgcache | awk '{ print $2 }' | xargs -n1 basename > $1/installed-packages.txt
	else
		printf "" > $1/installed-packages.txt
	fi

	# Produce dependency graph
	# First, quote each name to handle characters that cause issues for dot
	rootfs_list_installed_depends | sed 's:\([^| ]*\):"\1":g' > $1/depends.tmp
	# Change delimiter from pipe to -> and set style for recommend lines
	sed -i -e 's:|: -> :' -e 's:"\[REC\]":[style=dotted]:' -e 's:$:;:' $1/depends.tmp
	# Add header, sorted and de-duped contents and footer and then delete the temp file
	printf "digraph depends {\n    node [shape=plaintext]\n" > $1/depends.dot
	cat $1/depends.tmp | sort | uniq >> $1/depends.dot
	echo "}" >>  $1/depends.dot
	rm $1/depends.tmp

	# Produce installed package sizes list
	printf "" > $1/installed-package-sizes.tmp
	cat $pkgcache | while read pkg pkgfile pkgarch
	do
		for vendor in ${TARGET_VENDOR} ${MULTILIB_VENDORS} ; do
			size=`oe-pkgdata-util read-value ${PKGDATA_DIR} "PKGSIZE" ${pkg}_${pkgarch}`
			if [ "$size" != "" ] ; then
				echo "$size $pkg" >> $1/installed-package-sizes.tmp
			fi
		done
	done
	cat $1/installed-package-sizes.tmp | sort -n -r | awk '{print $1 "\tKiB " $2}' > $1/installed-package-sizes.txt
	rm $1/installed-package-sizes.tmp

	# We're now done with the cache, delete it
	rm $pkgcache

	if [ "$2" != "sdk" ] ; then
		# Produce some cut-down graphs (for readability)
		grep -v kernel_image $1/depends.dot | grep -v kernel-2 | grep -v kernel-3 > $1/depends-nokernel.dot
		grep -v libc6 $1/depends-nokernel.dot | grep -v libgcc > $1/depends-nokernel-nolibc.dot
		grep -v update- $1/depends-nokernel-nolibc.dot > $1/depends-nokernel-nolibc-noupdate.dot
		grep -v kernel-module $1/depends-nokernel-nolibc-noupdate.dot > $1/depends-nokernel-nolibc-noupdate-nomodules.dot
	fi

	# add complementary package information
	if [ -e ${WORKDIR}/complementary_pkgs.txt ]; then
		cp ${WORKDIR}/complementary_pkgs.txt $1
	fi
}

buildhistory_get_image_installed() {
	# Anything requiring the use of the packaging system should be done in here
	# in case the packaging files are going to be removed for this image

	if [ "${@base_contains('BUILDHISTORY_FEATURES', 'image', '1', '0', d)}" = "0" ] ; then
		return
	fi

	buildhistory_get_installed ${BUILDHISTORY_DIR_IMAGE}
}

buildhistory_get_sdk_installed() {
	# Anything requiring the use of the packaging system should be done in here
	# in case the packaging files are going to be removed for this SDK

	if [ "${@base_contains('BUILDHISTORY_FEATURES', 'sdk', '1', '0', d)}" = "0" ] ; then
		return
	fi

	buildhistory_get_installed ${BUILDHISTORY_DIR_SDK}/$1 sdk
}

buildhistory_list_files() {
	# List the files in the specified directory, but exclude date/time etc.
	# This awk script is somewhat messy, but handles where the size is not printed for device files under pseudo
	( cd $1 && find . -ls | awk '{ if ( $7 ~ /[0-9]/ ) printf "%s %10-s %10-s %10s %s %s %s\n", $3, $5, $6, $7, $11, $12, $13 ; else printf "%s %10-s %10-s %10s %s %s %s\n", $3, $5, $6, 0, $10, $11, $12 }' | sort -k5 | sed 's/ *$//' > $2 )
}


buildhistory_get_imageinfo() {
	if [ "${@base_contains('BUILDHISTORY_FEATURES', 'image', '1', '0', d)}" = "0" ] ; then
		return
	fi

	buildhistory_list_files ${IMAGE_ROOTFS} ${BUILDHISTORY_DIR_IMAGE}/files-in-image.txt

	# Collect files requested in BUILDHISTORY_IMAGE_FILES
	rm -rf ${BUILDHISTORY_DIR_IMAGE}/image-files
	for f in ${BUILDHISTORY_IMAGE_FILES}; do
		if [ -f ${IMAGE_ROOTFS}/$f ] ; then
			mkdir -p ${BUILDHISTORY_DIR_IMAGE}/image-files/`dirname $f`
			cp ${IMAGE_ROOTFS}/$f ${BUILDHISTORY_DIR_IMAGE}/image-files/$f
		fi
	done

	# Record some machine-readable meta-information about the image
	printf ""  > ${BUILDHISTORY_DIR_IMAGE}/image-info.txt
	cat >> ${BUILDHISTORY_DIR_IMAGE}/image-info.txt <<END
${@buildhistory_get_imagevars(d)}
END
	imagesize=`du -ks ${IMAGE_ROOTFS} | awk '{ print $1 }'`
	echo "IMAGESIZE = $imagesize" >> ${BUILDHISTORY_DIR_IMAGE}/image-info.txt

	# Add some configuration information
	echo "${MACHINE}: ${IMAGE_BASENAME} configured for ${DISTRO} ${DISTRO_VERSION}" > ${BUILDHISTORY_DIR_IMAGE}/build-id

	cat >> ${BUILDHISTORY_DIR_IMAGE}/build-id <<END
${@buildhistory_get_layers(d)}
END
}

buildhistory_get_sdkinfo() {
	if [ "${@base_contains('BUILDHISTORY_FEATURES', 'sdk', '1', '0', d)}" = "0" ] ; then
		return
	fi

	buildhistory_list_files ${SDK_OUTPUT} ${BUILDHISTORY_DIR_SDK}/files-in-sdk.txt

	# Record some machine-readable meta-information about the SDK
	printf ""  > ${BUILDHISTORY_DIR_SDK}/sdk-info.txt
	cat >> ${BUILDHISTORY_DIR_SDK}/sdk-info.txt <<END
${@buildhistory_get_sdkvars(d)}
END
	sdksize=`du -ks ${SDK_OUTPUT} | awk '{ print $1 }'`
	echo "SDKSIZE = $sdksize" >> ${BUILDHISTORY_DIR_SDK}/sdk-info.txt
}

# By prepending we get in before the removal of packaging files
ROOTFS_POSTPROCESS_COMMAND =+ "buildhistory_get_image_installed ; "

IMAGE_POSTPROCESS_COMMAND += " buildhistory_get_imageinfo ; "

# We want these to be the last run so that we get called after complementary package installation
POPULATE_SDK_POST_TARGET_COMMAND_append = "buildhistory_get_sdk_installed target ; "
POPULATE_SDK_POST_HOST_COMMAND_append = "buildhistory_get_sdk_installed host ; "

SDK_POSTPROCESS_COMMAND += "buildhistory_get_sdkinfo ; "

def buildhistory_get_layers(d):
    layertext = "Configured metadata layers:\n%s\n" % '\n'.join(get_layers_branch_rev(d))
    return layertext

def buildhistory_get_metadata_revs(d):
    # We want an easily machine-readable format here, so get_layers_branch_rev isn't quite what we want
    layers = (d.getVar("BBLAYERS", True) or "").split()
    medadata_revs = ["%-17s = %s:%s" % (os.path.basename(i), \
        base_get_metadata_git_branch(i, None).strip(), \
        base_get_metadata_git_revision(i, None)) \
            for i in layers]
    return '\n'.join(medadata_revs)


def squashspaces(string):
    import re
    return re.sub("\s+", " ", string).strip()

def outputvars(vars, listvars, d):
    vars = vars.split()
    listvars = listvars.split()
    ret = ""
    for var in vars:
        value = d.getVar(var, True) or ""
        if var in listvars:
            # Squash out spaces
            value = squashspaces(value)
        ret += "%s = %s\n" % (var, value)
    return ret.rstrip('\n')

def buildhistory_get_imagevars(d):
    imagevars = "DISTRO DISTRO_VERSION USER_CLASSES IMAGE_CLASSES IMAGE_FEATURES IMAGE_LINGUAS IMAGE_INSTALL BAD_RECOMMENDATIONS ROOTFS_POSTPROCESS_COMMAND IMAGE_POSTPROCESS_COMMAND"
    listvars = "USER_CLASSES IMAGE_CLASSES IMAGE_FEATURES IMAGE_LINGUAS IMAGE_INSTALL BAD_RECOMMENDATIONS"
    return outputvars(imagevars, listvars, d)

def buildhistory_get_sdkvars(d):
    sdkvars = "DISTRO DISTRO_VERSION SDK_NAME SDK_VERSION SDKMACHINE SDKIMAGE_FEATURES BAD_RECOMMENDATIONS"
    listvars = "SDKIMAGE_FEATURES BAD_RECOMMENDATIONS"
    return outputvars(sdkvars, listvars, d)


def buildhistory_get_cmdline(d):
    if sys.argv[0].endswith('bin/bitbake'):
        bincmd = 'bitbake'
    else:
        bincmd = sys.argv[0]
    return '%s %s' % (bincmd, ' '.join(sys.argv[1:]))


buildhistory_commit() {
	if [ ! -d ${BUILDHISTORY_DIR} ] ; then
		# Code above that creates this dir never executed, so there can't be anything to commit
		return
	fi

	# Create a machine-readable list of metadata revisions for each layer
	cat > ${BUILDHISTORY_DIR}/metadata-revs <<END
${@buildhistory_get_metadata_revs(d)}
END

	( cd ${BUILDHISTORY_DIR}/
		# Initialise the repo if necessary
		if [ ! -d .git ] ; then
			git init -q
		else
			git tag -f build-minus-3 build-minus-2 > /dev/null 2>&1 || true
			git tag -f build-minus-2 build-minus-1 > /dev/null 2>&1 || true
			git tag -f build-minus-1 > /dev/null 2>&1 || true
		fi
		# Check if there are new/changed files to commit (other than metadata-revs)
		repostatus=`git status --porcelain | grep -v " metadata-revs$"`
		HOSTNAME=`hostname 2>/dev/null || echo unknown`
		CMDLINE="${@buildhistory_get_cmdline(d)}"
		if [ "$repostatus" != "" ] ; then
			git add -A .
			# porcelain output looks like "?? packages/foo/bar"
			# Ensure we commit metadata-revs with the first commit
			for entry in `echo "$repostatus" | awk '{print $2}' | awk -F/ '{print $1}' | sort | uniq` ; do
				git commit $entry metadata-revs -m "$entry: Build ${BUILDNAME} of ${DISTRO} ${DISTRO_VERSION} for machine ${MACHINE} on $HOSTNAME" -m "cmd: $CMDLINE" --author "${BUILDHISTORY_COMMIT_AUTHOR}" > /dev/null
			done
			if [ "${BUILDHISTORY_PUSH_REPO}" != "" ] ; then
				git push -q ${BUILDHISTORY_PUSH_REPO}
			fi
		else
			git commit ${BUILDHISTORY_DIR}/ --allow-empty -m "No changes: Build ${BUILDNAME} of ${DISTRO} ${DISTRO_VERSION} for machine ${MACHINE} on $HOSTNAME" -m "cmd: $CMDLINE" --author "${BUILDHISTORY_COMMIT_AUTHOR}" > /dev/null
		fi) || true
}

python buildhistory_eventhandler() {
    if e.data.getVar('BUILDHISTORY_FEATURES', True).strip():
        if e.data.getVar("BUILDHISTORY_COMMIT", True) == "1":
            bb.note("Writing buildhistory")
            bb.build.exec_func("buildhistory_commit", e.data)
}

addhandler buildhistory_eventhandler
buildhistory_eventhandler[eventmask] = "bb.event.BuildCompleted"


# FIXME this ought to be moved into the fetcher
def _get_srcrev_values(d):
    """
    Return the version strings for the current recipe
    """

    scms = []
    fetcher = bb.fetch.Fetch(d.getVar('SRC_URI', True).split(), d)
    urldata = fetcher.ud
    for u in urldata:
        if urldata[u].method.supports_srcrev():
            scms.append(u)

    autoinc_templ = 'AUTOINC+'
    dict_srcrevs = {}
    dict_tag_srcrevs = {}
    for scm in scms:
        ud = urldata[scm]
        for name in ud.names:
            rev = ud.method.sortable_revision(scm, ud, d, name)
            # Clean this up when we next bump bitbake version
            if type(rev) != str:
                autoinc, rev = rev
            elif rev.startswith(autoinc_templ):
                rev = rev[len(autoinc_templ):]
            dict_srcrevs[name] = rev
            if 'tag' in ud.parm:
                tag = ud.parm['tag'];
                key = name+'_'+tag
                dict_tag_srcrevs[key] = rev
    return (dict_srcrevs, dict_tag_srcrevs)

do_fetch[postfuncs] += "write_srcrev"
python write_srcrev() {
    pkghistdir = d.getVar('BUILDHISTORY_DIR_PACKAGE', True)
    srcrevfile = os.path.join(pkghistdir, 'latest_srcrev')

    srcrevs, tag_srcrevs = _get_srcrev_values(d)
    if srcrevs:
        if not os.path.exists(pkghistdir):
            bb.utils.mkdirhier(pkghistdir)
        old_tag_srcrevs = {}
        if os.path.exists(srcrevfile):
            with open(srcrevfile) as f:
                for line in f:
                    if line.startswith('# tag_'):
                        key, value = line.split("=", 1)
                        key = key.replace('# tag_', '').strip()
                        value = value.replace('"', '').strip()
                        old_tag_srcrevs[key] = value
        with open(srcrevfile, 'w') as f:
            orig_srcrev = d.getVar('SRCREV', False) or 'INVALID'
            if orig_srcrev != 'INVALID':
                f.write('# SRCREV = "%s"\n' % orig_srcrev)
            if len(srcrevs) > 1:
                for name, srcrev in srcrevs.items():
                    orig_srcrev = d.getVar('SRCREV_%s' % name, False)
                    if orig_srcrev:
                        f.write('# SRCREV_%s = "%s"\n' % (name, orig_srcrev))
                    f.write('SRCREV_%s = "%s"\n' % (name, srcrev))
            else:
                f.write('SRCREV = "%s"\n' % srcrevs.itervalues().next())
            if len(tag_srcrevs) > 0:
                for name, srcrev in tag_srcrevs.items():
                    f.write('# tag_%s = "%s"\n' % (name, srcrev))
                    if name in old_tag_srcrevs and old_tag_srcrevs[name] != srcrev:
                        pkg = d.getVar('PN', True)
                        bb.warn("Revision for tag %s in package %s was changed since last build (from %s to %s)" % (name, pkg, old_tag_srcrevs[name], srcrev))

    else:
        if os.path.exists(srcrevfile):
            os.remove(srcrevfile)
}
