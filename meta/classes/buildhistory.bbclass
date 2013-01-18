#
# Records history of build output in order to detect regressions
#
# Based in part on testlab.bbclass and packagehistory.bbclass
#
# Copyright (C) 2011 Intel Corporation
# Copyright (C) 2007-2011 Koen Kooi <koen@openembedded.org>
#

BUILDHISTORY_FEATURES ?= "image package"
BUILDHISTORY_DIR ?= "${TMPDIR}/buildhistory"
BUILDHISTORY_DIR_IMAGE = "${BUILDHISTORY_DIR}/images/${MACHINE_ARCH}/${TCLIBC}/${IMAGE_BASENAME}"
BUILDHISTORY_DIR_PACKAGE = "${BUILDHISTORY_DIR}/packages/${MULTIMACH_TARGET_SYS}/${PN}"
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
            self.rdepends = ""
            self.rrecommends = ""
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
                elif name == "RDEPENDS":
                    pkginfo.rdepends = value
                elif name == "RRECOMMENDS":
                    pkginfo.rrecommends = value
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
    packages = squashspaces(d.getVar('PACKAGES', True))

    packagelist = packages.split()
    if not os.path.exists(pkghistdir):
        os.makedirs(pkghistdir)
    else:
        # Remove files for packages that no longer exist
        for item in os.listdir(pkghistdir):
            if item != "latest":
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
                bb.error("Package version for package %s went backwards which would break package feeds from (%s:%s-%s to %s:%s-%s)" % (pkg, last_pkge, last_pkgv, last_pkgr, pkge, pkgv, pkgr))

        pkginfo = PackageInfo(pkg)
        # Apparently the version can be different on a per-package basis (see Python)
        pkginfo.pe = getpkgvar(pkg, 'PE') or "0"
        pkginfo.pv = getpkgvar(pkg, 'PV')
        pkginfo.pr = getpkgvar(pkg, 'PR')
        pkginfo.pkg = getpkgvar(pkg, 'PKG') or pkg
        pkginfo.pkge = pkge
        pkginfo.pkgv = pkgv
        pkginfo.pkgr = pkgr
        pkginfo.rdepends = sortpkglist(squashspaces(getpkgvar(pkg, 'RDEPENDS') or ""))
        pkginfo.rrecommends = sortpkglist(squashspaces(getpkgvar(pkg, 'RRECOMMENDS') or ""))
        pkginfo.files = squashspaces(getpkgvar(pkg, 'FILES') or "")
        for filevar in pkginfo.filevars:
            pkginfo.filevars[filevar] = getpkgvar(pkg, filevar)

        # Gather information about packaged files
        pkgdestpkg = os.path.join(pkgdest, pkg)
        filelist = []
        pkginfo.size = 0
        for root, dirs, files in os.walk(pkgdestpkg):
            relpth = os.path.relpath(root, pkgdestpkg)
            for f in files:
                fstat = os.lstat(os.path.join(root, f))
                pkginfo.size += fstat.st_size
                filelist.append(os.sep + os.path.join(relpth, f))
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
        os.makedirs(pkgpath)

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

        f.write("RDEPENDS = %s\n" %  pkginfo.rdepends)
        f.write("RRECOMMENDS = %s\n" %  pkginfo.rrecommends)
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


buildhistory_get_image_installed() {
	# Anything requiring the use of the packaging system should be done in here
	# in case the packaging files are going to be removed for this image

	if [ "${@base_contains('BUILDHISTORY_FEATURES', 'image', '1', '0', d)}" = "0" ] ; then
		return
	fi

	mkdir -p ${BUILDHISTORY_DIR_IMAGE}

	# Get list of installed packages
	pkgcache="${BUILDHISTORY_DIR_IMAGE}/installed-packages.tmp"
	list_installed_packages file | sort > $pkgcache

	cat $pkgcache | awk '{ print $1 }' > ${BUILDHISTORY_DIR_IMAGE}/installed-package-names.txt
	cat $pkgcache | awk '{ print $2 }' | xargs -n1 basename > ${BUILDHISTORY_DIR_IMAGE}/installed-packages.txt

	# Produce dependency graph
	# First, filter out characters that cause issues for dot
	rootfs_list_installed_depends | sed -e 's:-:_:g' -e 's:\.:_:g' -e 's:+::g' > ${BUILDHISTORY_DIR_IMAGE}/depends.tmp
	# Change delimiter from pipe to -> and set style for recommend lines
	sed -i -e 's:|: -> :' -e 's:\[REC\]:[style=dotted]:' -e 's:$:;:' ${BUILDHISTORY_DIR_IMAGE}/depends.tmp
	# Add header, sorted and de-duped contents and footer and then delete the temp file
	printf "digraph depends {\n    node [shape=plaintext]\n" > ${BUILDHISTORY_DIR_IMAGE}/depends.dot
	cat ${BUILDHISTORY_DIR_IMAGE}/depends.tmp | sort | uniq >> ${BUILDHISTORY_DIR_IMAGE}/depends.dot
	echo "}" >>  ${BUILDHISTORY_DIR_IMAGE}/depends.dot
	rm ${BUILDHISTORY_DIR_IMAGE}/depends.tmp

	# Produce installed package sizes list
	printf "" > ${BUILDHISTORY_DIR_IMAGE}/installed-package-sizes.tmp
	cat $pkgcache | while read pkg pkgfile
	do
		if [ -f $pkgfile ] ; then
			pkgsize=`du -k $pkgfile | head -n1 | awk '{ print $1 }'`
			echo $pkgsize $pkg >> ${BUILDHISTORY_DIR_IMAGE}/installed-package-sizes.tmp
		fi
	done
	cat ${BUILDHISTORY_DIR_IMAGE}/installed-package-sizes.tmp | sort -n -r | awk '{print $1 "\tKiB " $2}' > ${BUILDHISTORY_DIR_IMAGE}/installed-package-sizes.txt
	rm ${BUILDHISTORY_DIR_IMAGE}/installed-package-sizes.tmp

	# We're now done with the cache, delete it
	rm $pkgcache

	# Produce some cut-down graphs (for readability)
	grep -v kernel_image ${BUILDHISTORY_DIR_IMAGE}/depends.dot | grep -v kernel_2 | grep -v kernel_3 > ${BUILDHISTORY_DIR_IMAGE}/depends-nokernel.dot
	grep -v libc6 ${BUILDHISTORY_DIR_IMAGE}/depends-nokernel.dot | grep -v libgcc > ${BUILDHISTORY_DIR_IMAGE}/depends-nokernel-nolibc.dot
	grep -v update_ ${BUILDHISTORY_DIR_IMAGE}/depends-nokernel-nolibc.dot > ${BUILDHISTORY_DIR_IMAGE}/depends-nokernel-nolibc-noupdate.dot
	grep -v kernel_module ${BUILDHISTORY_DIR_IMAGE}/depends-nokernel-nolibc-noupdate.dot > ${BUILDHISTORY_DIR_IMAGE}/depends-nokernel-nolibc-noupdate-nomodules.dot

	# add complementary package information
	if [ -e ${WORKDIR}/complementary_pkgs.txt ]; then
		cp ${WORKDIR}/complementary_pkgs.txt ${BUILDHISTORY_DIR_IMAGE}
	fi
}

buildhistory_get_imageinfo() {
	if [ "${@base_contains('BUILDHISTORY_FEATURES', 'image', '1', '0', d)}" = "0" ] ; then
		return
	fi

	# List the files in the image, but exclude date/time etc.
	# This awk script is somewhat messy, but handles where the size is not printed for device files under pseudo
	( cd ${IMAGE_ROOTFS} && find . -ls | awk '{ if ( $7 ~ /[0-9]/ ) printf "%s %10-s %10-s %10s %s %s %s\n", $3, $5, $6, $7, $11, $12, $13 ; else printf "%s %10-s %10-s %10s %s %s %s\n", $3, $5, $6, 0, $10, $11, $12 }' | sort -k5 > ${BUILDHISTORY_DIR_IMAGE}/files-in-image.txt )

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

# By prepending we get in before the removal of packaging files
ROOTFS_POSTPROCESS_COMMAND =+ "buildhistory_get_image_installed ; "

IMAGE_POSTPROCESS_COMMAND += " buildhistory_get_imageinfo ; "

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


def buildhistory_get_imagevars(d):
    imagevars = "DISTRO DISTRO_VERSION USER_CLASSES IMAGE_CLASSES IMAGE_FEATURES IMAGE_LINGUAS IMAGE_INSTALL BAD_RECOMMENDATIONS ROOTFS_POSTPROCESS_COMMAND IMAGE_POSTPROCESS_COMMAND"
    listvars = "USER_CLASSES IMAGE_CLASSES IMAGE_FEATURES IMAGE_LINGUAS IMAGE_INSTALL BAD_RECOMMENDATIONS"

    imagevars = imagevars.split()
    listvars = listvars.split()
    ret = ""
    for var in imagevars:
        value = d.getVar(var, True) or ""
        if var in listvars:
            # Squash out spaces
            value = squashspaces(value)
        ret += "%s = %s\n" % (var, value)
    return ret.rstrip('\n')


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
		fi
		# Check if there are new/changed files to commit (other than metadata-revs)
		repostatus=`git status --porcelain | grep -v " metadata-revs$"`
		HOSTNAME=`hostname 2>/dev/null || echo unknown`
		if [ "$repostatus" != "" ] ; then
			git add .
			# porcelain output looks like "?? packages/foo/bar"
			# Ensure we commit metadata-revs with the first commit
			for entry in `echo "$repostatus" | awk '{print $2}' | awk -F/ '{print $1}' | sort | uniq` ; do
				git commit $entry metadata-revs -m "$entry: Build ${BUILDNAME} of ${DISTRO} ${DISTRO_VERSION} for machine ${MACHINE} on $HOSTNAME" --author "${BUILDHISTORY_COMMIT_AUTHOR}" > /dev/null
			done
			if [ "${BUILDHISTORY_PUSH_REPO}" != "" ] ; then
				git push -q ${BUILDHISTORY_PUSH_REPO}
			fi
		else
			git commit ${BUILDHISTORY_DIR}/ --allow-empty -m "No changes: Build ${BUILDNAME} of ${DISTRO} ${DISTRO_VERSION} for machine ${MACHINE} on $HOSTNAME" --author "${BUILDHISTORY_COMMIT_AUTHOR}" > /dev/null
		fi) || true
}

python buildhistory_eventhandler() {
    if isinstance(e, bb.event.BuildCompleted):
        if e.data.getVar('BUILDHISTORY_FEATURES', True).strip():
            if e.data.getVar("BUILDHISTORY_COMMIT", True) == "1":
                bb.build.exec_func("buildhistory_commit", e.data)
}

addhandler buildhistory_eventhandler
