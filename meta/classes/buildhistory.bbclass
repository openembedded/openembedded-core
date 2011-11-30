#
# Records history of build output in order to detect regressions
#
# Based in part on testlab.bbclass
#
# Copyright (C) 2011 Intel Corporation
# Copyright (C) 2007-2011 Koen Kooi <koen@openembedded.org>
#

BUILDHISTORY_DIR ?= "${TMPDIR}/buildhistory"
BUILDHISTORY_DIR_IMAGE = "${BUILDHISTORY_DIR}/images/${MACHINE_ARCH}/${TCLIBC}/${IMAGE_BASENAME}"
BUILDHISTORY_COMMIT ?= "0"
BUILDHISTORY_COMMIT_AUTHOR ?= "buildhistory <buildhistory@${DISTRO}>"
BUILDHISTORY_PUSH_REPO ?= ""

buildhistory_get_image_installed() {
	# Anything requiring the use of the packaging system should be done in here
	# in case the packaging files are going to be removed for this image

	mkdir -p ${BUILDHISTORY_DIR_IMAGE}

	# Get list of installed packages
	list_installed_packages | sort > ${BUILDHISTORY_DIR_IMAGE}/installed-package-names.txt
	INSTALLED_PKGS=`cat ${BUILDHISTORY_DIR_IMAGE}/installed-package-names.txt`

	# Produce installed package file and size lists and dependency graph
	echo -n > ${BUILDHISTORY_DIR_IMAGE}/installed-packages.txt
	echo -n > ${BUILDHISTORY_DIR_IMAGE}/installed-package-sizes.tmp
	echo -e "digraph depends {\n    node [shape=plaintext]" > ${BUILDHISTORY_DIR_IMAGE}/depends.dot
	for pkg in $INSTALLED_PKGS; do
		pkgfile=`get_package_filename $pkg`
		echo `basename $pkgfile` >> ${BUILDHISTORY_DIR_IMAGE}/installed-packages.txt
		if [ -f $pkgfile ] ; then
			pkgsize=`du -k $pkgfile | head -n1 | awk '{ print $1 }'`
			echo $pkgsize $pkg >> ${BUILDHISTORY_DIR_IMAGE}/installed-package-sizes.tmp
		fi

		deps=`list_package_depends $pkg`
		for dep in $deps ; do
			echo "$pkg OPP $dep;" | sed -e 's:-:_:g' -e 's:\.:_:g' -e 's:+::g' | sed 's:OPP:->:g' >> ${BUILDHISTORY_DIR_IMAGE}/depends.dot
		done

		recs=`list_package_recommends $pkg`
		for rec in $recs ; do
			echo "$pkg OPP $rec [style=dotted];" | sed -e 's:-:_:g' -e 's:\.:_:g' -e 's:+::g' | sed 's:OPP:->:g' >> ${BUILDHISTORY_DIR_IMAGE}/depends.dot
		done
	done
	echo "}" >>  ${BUILDHISTORY_DIR_IMAGE}/depends.dot

	cat ${BUILDHISTORY_DIR_IMAGE}/installed-package-sizes.tmp | sort -n -r | awk '{print $1 "\tKiB " $2}' > ${BUILDHISTORY_DIR_IMAGE}/installed-package-sizes.txt
	rm ${BUILDHISTORY_DIR_IMAGE}/installed-package-sizes.tmp

	# Produce some cut-down graphs (for readability)
	grep -v kernel_image ${BUILDHISTORY_DIR_IMAGE}/depends.dot | grep -v kernel_2 | grep -v kernel_3 > ${BUILDHISTORY_DIR_IMAGE}/depends-nokernel.dot
	grep -v libc6 ${BUILDHISTORY_DIR_IMAGE}/depends-nokernel.dot | grep -v libgcc > ${BUILDHISTORY_DIR_IMAGE}/depends-nokernel-nolibc.dot
	grep -v update_ ${BUILDHISTORY_DIR_IMAGE}/depends-nokernel-nolibc.dot > ${BUILDHISTORY_DIR_IMAGE}/depends-nokernel-nolibc-noupdate.dot
	grep -v kernel_module ${BUILDHISTORY_DIR_IMAGE}/depends-nokernel-nolibc-noupdate.dot > ${BUILDHISTORY_DIR_IMAGE}/depends-nokernel-nolibc-noupdate-nomodules.dot

	# Workaround for broken shell function dependencies
	if false ; then
		get_package_filename
		list_package_depends
		list_package_recommends
	fi
}

buildhistory_get_imageinfo() {
	# List the files in the image, but exclude date/time etc.
	# This awk script is somewhat messy, but handles where the size is not printed for device files under pseudo
	find ${IMAGE_ROOTFS} -ls | awk '{ if ( $7 ~ /[0-9]/ ) printf "%s %10-s %10-s %10s %s %s %s\n", $3, $5, $6, $7, $11, $12, $13 ; else printf "%s %10-s %10-s %10s %s %s %s\n", $3, $5, $6, 0, $10, $11, $12 }' > ${BUILDHISTORY_DIR_IMAGE}/files-in-image.txt

	# Add some configuration information
	echo "${MACHINE}: ${IMAGE_BASENAME} configured for ${DISTRO} ${DISTRO_VERSION}" > ${BUILDHISTORY_DIR_IMAGE}/build-id
	echo "${@buildhistory_get_layers(d)}" >> ${BUILDHISTORY_DIR_IMAGE}/build-id
}

# By prepending we get in before the removal of packaging files
ROOTFS_POSTPROCESS_COMMAND =+ "buildhistory_get_image_installed ; "

IMAGE_POSTPROCESS_COMMAND += " buildhistory_get_imageinfo ; "

def buildhistory_get_layers(d):
	layertext = "Configured metadata layers:\n%s\n" % '\n'.join(get_layers_branch_rev(d))
	return layertext


buildhistory_commit() {
	( cd ${BUILDHISTORY_DIR}/
		git add ${BUILDHISTORY_DIR}/*
		git commit ${BUILDHISTORY_DIR}/ -m "Build ${BUILDNAME} for machine ${MACHINE} configured for ${DISTRO} ${DISTRO_VERSION}" --author "${BUILDHISTORY_COMMIT_AUTHOR}" > /dev/null
		if [ "${BUILDHISTORY_PUSH_REPO}" != "" ] ; then
			git push -q ${BUILDHISTORY_PUSH_REPO}
		fi) || true
}

python buildhistory_eventhandler() {
	import bb.build
	import bb.event

	if isinstance(e, bb.event.BuildCompleted):
		if e.data.getVar("BUILDHISTORY_COMMIT", True) == "1":
			bb.build.exec_func("buildhistory_commit", e.data)
}

addhandler buildhistory_eventhandler
