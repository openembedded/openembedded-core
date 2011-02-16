inherit rootfs_${IMAGE_PKGTYPE}

IMAGETEST ?= "dummy"
inherit imagetest-${IMAGETEST}

LICENSE = "MIT"
PACKAGES = ""
RDEPENDS += "${IMAGE_INSTALL}"

INHIBIT_DEFAULT_DEPS = "1"

# "export IMAGE_BASENAME" not supported at this time
IMAGE_BASENAME[export] = "1"
export PACKAGE_INSTALL ?= "${IMAGE_INSTALL}"
PACKAGE_INSTALL_ATTEMPTONLY ?= ""

# We need to recursively follow RDEPENDS and RRECOMMENDS for images
do_rootfs[recrdeptask] += "do_deploy do_populate_sysroot"

# Images are generally built explicitly, do not need to be part of world.
EXCLUDE_FROM_WORLD = "1"

USE_DEVFS ?= "0"

PID = "${@os.getpid()}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_rootfs[depends] += "makedevs-native:do_populate_sysroot virtual/fakeroot-native:do_populate_sysroot ldconfig-native:do_populate_sysroot"

python () {
    deps = bb.data.getVarFlag('do_rootfs', 'depends', d) or ""
    for type in (bb.data.getVar('IMAGE_FSTYPES', d, True) or "").split():
        for dep in ((bb.data.getVar('IMAGE_DEPENDS_%s' % type, d) or "").split() or []):
            deps += " %s:do_populate_sysroot" % dep
    for dep in (bb.data.getVar('EXTRA_IMAGEDEPENDS', d, True) or "").split():
        deps += " %s:do_populate_sysroot" % dep
    bb.data.setVarFlag('do_rootfs', 'depends', deps, d)

    # If we don't do this we try and run the mapping hooks while parsing which is slow
    # bitbake should really provide something to let us know this...
    if bb.data.getVar('BB_WORKERCONTEXT', d, True) is not None:
        runtime_mapping_rename("PACKAGE_INSTALL", d)
        runtime_mapping_rename("PACKAGE_INSTALL_ATTEMPTONLY", d)
}

#
# Get a list of files containing device tables to create.
# * IMAGE_DEVICE_TABLE is the old name to an absolute path to a device table file
# * IMAGE_DEVICE_TABLES is a new name for a file, or list of files, seached
#   for in the BBPATH
# If neither are specified then the default name of files/device_table-minimal.txt
# is searched for in the BBPATH (same as the old version.)
#
def get_devtable_list(d):
    devtable = bb.data.getVar('IMAGE_DEVICE_TABLE', d, 1)
    if devtable != None:
        return devtable
    str = ""
    devtables = bb.data.getVar('IMAGE_DEVICE_TABLES', d, 1)
    if devtables == None:
        devtables = 'files/device_table-minimal.txt'
    for devtable in devtables.split():
        str += " %s" % bb.which(bb.data.getVar('BBPATH', d, 1), devtable)
    return str

def get_imagecmds(d):
    cmds = "\n"
    old_overrides = bb.data.getVar('OVERRIDES', d, 0)
    for type in bb.data.getVar('IMAGE_FSTYPES', d, True).split():
        localdata = bb.data.createCopy(d)
        bb.data.setVar('OVERRIDES', '%s:%s' % (type, old_overrides), localdata)
        bb.data.update_data(localdata)
        cmd  = "\t#Code for image type " + type + "\n"
        cmd += "\t${IMAGE_CMD_" + type + "}\n"
        cmd += "\tcd ${DEPLOY_DIR_IMAGE}/\n"
        cmd += "\trm -f ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}." + type + "\n"
        cmd += "\tln -s ${IMAGE_NAME}.rootfs." + type + " ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}." + type + "\n\n"
        cmds += bb.data.expand(cmd, localdata)
    return cmds

IMAGE_POSTPROCESS_COMMAND ?= ""
MACHINE_POSTPROCESS_COMMAND ?= ""
ROOTFS_POSTPROCESS_COMMAND ?= ""

# some default locales
IMAGE_LINGUAS ?= "de-de fr-fr en-gb"

LINGUAS_INSTALL = "${@" ".join(map(lambda s: "locale-base-%s" % s, bb.data.getVar('IMAGE_LINGUAS', d, 1).split()))}"

do_rootfs[nostamp] = "1"
do_rootfs[dirs] = "${TOPDIR}"
do_rootfs[lockfiles] += "${IMAGE_ROOTFS}.lock"
do_build[nostamp] = "1"

# Must call real_do_rootfs() from inside here, rather than as a separate
# task, so that we have a single fakeroot context for the whole process.
fakeroot do_rootfs () {
	#set -x
	rm -rf ${IMAGE_ROOTFS}
	mkdir -p ${IMAGE_ROOTFS}
	mkdir -p ${DEPLOY_DIR_IMAGE}

	if [ "${USE_DEVFS}" != "1" ]; then
		for devtable in ${@get_devtable_list(d)}; do
			makedevs -r ${IMAGE_ROOTFS} -D $devtable
		done
	fi

	rootfs_${IMAGE_PKGTYPE}_do_rootfs

	insert_feed_uris

	# Run ldconfig on the image to create a valid cache 
	# (new format for cross arch compatibility)
	ldconfig -r ${IMAGE_ROOTFS} -c new

	# (re)create kernel modules dependencies
	# This part is done by kernel-module-* postinstall scripts but if image do
	# not contains modules at all there are few moments in boot sequence with
	# "unable to open modules.dep" message.
	if [ -e ${STAGING_KERNEL_DIR}/kernel-abiversion ]; then
		KERNEL_VERSION=`cat ${STAGING_KERNEL_DIR}/kernel-abiversion`

		mkdir -p ${IMAGE_ROOTFS}/lib/modules/$KERNEL_VERSION
		${TARGET_SYS}-depmod-2.6 -a -b ${IMAGE_ROOTFS} -F ${STAGING_KERNEL_DIR}/System.map-$KERNEL_VERSION $KERNEL_VERSION
	fi

	${IMAGE_PREPROCESS_COMMAND}

	ROOTFS_SIZE=`du -ks ${IMAGE_ROOTFS}|awk '{size = $1 * ${IMAGE_OVERHEAD_FACTOR}; print (size > ${IMAGE_ROOTFS_SIZE} ? size : ${IMAGE_ROOTFS_SIZE}) }'`
	${@get_imagecmds(d)}

	${IMAGE_POSTPROCESS_COMMAND}
	
	${MACHINE_POSTPROCESS_COMMAND}
}

insert_feed_uris () {
	
	echo "Building feeds for [${DISTRO}].."

	for line in ${FEED_URIS}
	do
		# strip leading and trailing spaces/tabs, then split into name and uri
		line_clean="`echo "$line"|sed 's/^[ \t]*//;s/[ \t]*$//'`"
		feed_name="`echo "$line_clean" | sed -n 's/\(.*\)##\(.*\)/\1/p'`"
		feed_uri="`echo "$line_clean" | sed -n 's/\(.*\)##\(.*\)/\2/p'`"
		
		echo "Added $feed_name feed with URL $feed_uri"
		
		# insert new feed-sources
		echo "src/gz $feed_name $feed_uri" >> ${IMAGE_ROOTFS}/etc/opkg/${feed_name}-feed.conf
	done
}

log_check() {
	for target in $*
	do
		lf_path="${WORKDIR}/temp/log.do_$target.${PID}"
		
		echo "log_check: Using $lf_path as logfile"
		
		if test -e "$lf_path"
		then
			${IMAGE_PKGTYPE}_log_check $target $lf_path
		else
			echo "Cannot find logfile [$lf_path]"
		fi
		echo "Logfile is clean"
	done
}

# set '*' as the rootpassword so the images
# can decide if they want it or not

zap_root_password () {
	sed 's%^root:[^:]*:%root:*:%' < ${IMAGE_ROOTFS}/etc/passwd >${IMAGE_ROOTFS}/etc/passwd.new
	mv ${IMAGE_ROOTFS}/etc/passwd.new ${IMAGE_ROOTFS}/etc/passwd
} 

# Turn any symbolic /sbin/init link into a file
remove_init_link () {
	if [ -h ${IMAGE_ROOTFS}/sbin/init ]; then
		LINKFILE=${IMAGE_ROOTFS}`readlink ${IMAGE_ROOTFS}/sbin/init`
		rm ${IMAGE_ROOTFS}/sbin/init
		cp $LINKFILE ${IMAGE_ROOTFS}/sbin/init
	fi
}

make_zimage_symlink_relative () {
	if [ -L ${IMAGE_ROOTFS}/boot/zImage ]; then
		(cd ${IMAGE_ROOTFS}/boot/ && for i in `ls zImage-* | sort`; do ln -sf $i zImage; done)
	fi
}

write_image_manifest () {
	rootfs_${IMAGE_PKGTYPE}_write_manifest

	rm -f ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.manifest
        ln -s ${IMAGE_NAME}.rootfs.manifest ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.manifest
}

# Make login manager(s) enable automatic login.
# Useful for devices where we do not want to log in at all (e.g. phones)
set_image_autologin () {
        sed -i 's%^AUTOLOGIN=\"false"%AUTOLOGIN="true"%g' ${IMAGE_ROOTFS}/etc/sysconfig/gpelogin
}

# Can be use to create /etc/timestamp during image construction to give a reasonably 
# sane default time setting
rootfs_update_timestamp () {
	date -u +%2m%2d%2H%2M%4Y >${IMAGE_ROOTFS}/etc/timestamp
}

# Prevent X from being started
rootfs_no_x_startup () {
	if [ -f ${IMAGE_ROOTFS}/etc/init.d/xserver-nodm ]; then
		chmod a-x ${IMAGE_ROOTFS}/etc/init.d/xserver-nodm
	fi
}

rootfs_trim_schemas () {
	for schema in ${IMAGE_ROOTFS}/etc/gconf/schemas/*.schemas
	do
		# Need this in case no files exist
		if [ -e $schema ]; then
			poky-trim-schemas $schema > $schema.new
        	        mv $schema.new $schema
		fi
	done
}


# export the zap_root_password, and remote_init_link
EXPORT_FUNCTIONS zap_root_password remove_init_link do_rootfs make_zimage_symlink_relative set_image_autologin rootfs_update_timestamp rootfs_no_x_startup

addtask rootfs before do_build after do_install
