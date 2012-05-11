inherit rootfs_${IMAGE_PKGTYPE}

IMAGETEST ?= "dummy"
inherit imagetest-${IMAGETEST}

inherit gzipnative

LICENSE = "MIT"
PACKAGES = ""
RDEPENDS += "${IMAGE_INSTALL} ${LINGUAS_INSTALL} ${NORMAL_FEATURE_INSTALL} ${ROOTFS_BOOTSTRAP_INSTALL}"
RRECOMMENDS += "${NORMAL_FEATURE_INSTALL_OPTIONAL}"

INHIBIT_DEFAULT_DEPS = "1"

# IMAGE_FEATURES may contain any available package group
IMAGE_FEATURES ?= ""
IMAGE_FEATURES[type] = "list"

# rootfs bootstrap install
ROOTFS_BOOTSTRAP_INSTALL = "${@base_contains("IMAGE_FEATURES", "package-management", "", "${ROOTFS_PKGMANAGE_BOOTSTRAP}",d)}"

# packages to install from features
FEATURE_INSTALL = "${@' '.join(oe.packagegroup.required_packages(oe.data.typed_value('IMAGE_FEATURES', d), d))}"
FEATURE_INSTALL_OPTIONAL = "${@' '.join(oe.packagegroup.optional_packages(oe.data.typed_value('IMAGE_FEATURES', d), d))}"

# packages to install from features, excluding dev/dbg/doc
NORMAL_FEATURE_INSTALL = "${@' '.join(oe.packagegroup.required_packages(normal_groups(d), d))}"
NORMAL_FEATURE_INSTALL_OPTIONAL = "${@' '.join(oe.packagegroup.optional_packages(normal_groups(d), d))}"

def normal_groups(d):
    """Return all the IMAGE_FEATURES, with the exception of our special package groups"""
    extras = set(['dev-pkgs', 'doc-pkgs', 'dbg-pkgs'])
    features = set(oe.data.typed_value('IMAGE_FEATURES', d))
    return features.difference(extras)

def normal_pkgs_to_install(d):
    import oe.packagedata

    to_install = oe.data.typed_value('IMAGE_INSTALL', d)
    features = normal_groups(d)
    required = list(oe.packagegroup.required_packages(features, d))
    optional = list(oe.packagegroup.optional_packages(features, d))
    all_packages = to_install + required + optional

    recipes = filter(None, [oe.packagedata.recipename(pkg, d) for pkg in all_packages])

    return all_packages + recipes

PACKAGE_GROUP_dbg-pkgs = "${@' '.join('%s-dbg' % pkg for pkg in normal_pkgs_to_install(d))}"
PACKAGE_GROUP_dbg-pkgs[optional] = "1"
PACKAGE_GROUP_dev-pkgs = "${@' '.join('%s-dev' % pkg for pkg in normal_pkgs_to_install(d))}"
PACKAGE_GROUP_dev-pkgs[optional] = "1"
PACKAGE_GROUP_doc-pkgs = "${@' '.join('%s-doc' % pkg for pkg in normal_pkgs_to_install(d))}"
PACKAGE_GROUP_doc-pkgs[optional] = "1"

# "export IMAGE_BASENAME" not supported at this time
IMAGE_INSTALL ?= ""
IMAGE_INSTALL[type] = "list"
IMAGE_BASENAME[export] = "1"
export PACKAGE_INSTALL ?= "${IMAGE_INSTALL} ${ROOTFS_BOOTSTRAP_INSTALL} ${FEATURE_INSTALL}"
PACKAGE_INSTALL_ATTEMPTONLY ?= "${FEATURE_INSTALL_OPTIONAL}"

# Images are generally built explicitly, do not need to be part of world.
EXCLUDE_FROM_WORLD = "1"

USE_DEVFS ?= "0"

PID = "${@os.getpid()}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

LDCONFIGDEPEND ?= "ldconfig-native:do_populate_sysroot"
LDCONFIGDEPEND_libc-uclibc = ""

do_rootfs[depends] += "makedevs-native:do_populate_sysroot virtual/fakeroot-native:do_populate_sysroot ${LDCONFIGDEPEND}"
do_rootfs[depends] += "virtual/update-alternatives-native:do_populate_sysroot update-rc.d-native:do_populate_sysroot"

IMAGE_TYPE_live = '${@base_contains("IMAGE_FSTYPES", "live", "live", "empty", d)}'
inherit image-${IMAGE_TYPE_live}
IMAGE_TYPE_vmdk = '${@base_contains("IMAGE_FSTYPES", "vmdk", "vmdk", "empty", d)}'
inherit image-${IMAGE_TYPE_vmdk}

python () {
    deps = d.getVarFlag('do_rootfs', 'depends') or ""
    deps += imagetypes_getdepends(d)
    for dep in (d.getVar('EXTRA_IMAGEDEPENDS', True) or "").split():
        deps += " %s:do_populate_sysroot" % dep
    d.setVarFlag('do_rootfs', 'depends', deps)

    # If we don't do this we try and run the mapping hooks while parsing which is slow
    # bitbake should really provide something to let us know this...
    if d.getVar('BB_WORKERCONTEXT', True) is not None:
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
    devtable = d.getVar('IMAGE_DEVICE_TABLE', True)
    if devtable != None:
        return devtable
    str = ""
    devtables = d.getVar('IMAGE_DEVICE_TABLES', True)
    if devtables == None:
        devtables = 'files/device_table-minimal.txt'
    for devtable in devtables.split():
        str += " %s" % bb.which(d.getVar('BBPATH', True), devtable)
    return str

IMAGE_CLASSES ?= "image_types"
inherit ${IMAGE_CLASSES}

IMAGE_POSTPROCESS_COMMAND ?= ""
MACHINE_POSTPROCESS_COMMAND ?= ""
ROOTFS_POSTPROCESS_COMMAND ?= ""

# some default locales
IMAGE_LINGUAS ?= "de-de fr-fr en-gb"

LINGUAS_INSTALL ?= "${@" ".join(map(lambda s: "locale-base-%s" % s, d.getVar('IMAGE_LINGUAS', True).split()))}"

PSEUDO_PASSWD = "${IMAGE_ROOTFS}"

do_rootfs[nostamp] = "1"
do_rootfs[dirs] = "${TOPDIR}"
do_rootfs[lockfiles] += "${IMAGE_ROOTFS}.lock"
do_rootfs[cleandirs] += "${S}"
do_build[nostamp] = "1"

# Must call real_do_rootfs() from inside here, rather than as a separate
# task, so that we have a single fakeroot context for the whole process.
do_rootfs[umask] = "022"

fakeroot do_rootfs () {
	#set -x
    # When use the rpm incremental image generation, don't remove the rootfs
    if [ "${INC_RPM_IMAGE_GEN}" != "1" -o "${IMAGE_PKGTYPE}" != "rpm" ]; then
        rm -rf ${IMAGE_ROOTFS}
    fi
	rm -rf ${MULTILIB_TEMP_ROOTFS}
	mkdir -p ${IMAGE_ROOTFS}
	mkdir -p ${DEPLOY_DIR_IMAGE}

	cp ${COREBASE}/meta/files/deploydir_readme.txt ${DEPLOY_DIR_IMAGE}/README_-_DO_NOT_DELETE_FILES_IN_THIS_DIRECTORY.txt || true

    # If "${IMAGE_ROOTFS}/dev" exists, then the device had been made by
    # the previous build
	if [ "${USE_DEVFS}" != "1" -a ! -r "${IMAGE_ROOTFS}/dev" ]; then
		for devtable in ${@get_devtable_list(d)}; do
            # Always return ture since there maybe already one when use the
            # incremental image generation
			makedevs -r ${IMAGE_ROOTFS} -D $devtable
		done
	fi

	rootfs_${IMAGE_PKGTYPE}_do_rootfs

	insert_feed_uris

	if [ "x${LDCONFIGDEPEND}" != "x" ]; then
		# Run ldconfig on the image to create a valid cache 
		# (new format for cross arch compatibility)
		echo executing: ldconfig -r ${IMAGE_ROOTFS} -c new -v
		ldconfig -r ${IMAGE_ROOTFS} -c new -v
	fi

	# (re)create kernel modules dependencies
	# This part is done by kernel-module-* postinstall scripts but if image do
	# not contains modules at all there are few moments in boot sequence with
	# "unable to open modules.dep" message.
	if [ -e ${STAGING_KERNEL_DIR}/kernel-abiversion ]; then
		KERNEL_VERSION=`cat ${STAGING_KERNEL_DIR}/kernel-abiversion`

		mkdir -p ${IMAGE_ROOTFS}/lib/modules/$KERNEL_VERSION
		${TARGET_PREFIX}depmod -a -b ${IMAGE_ROOTFS} -F ${STAGING_KERNEL_DIR}/System.map-$KERNEL_VERSION $KERNEL_VERSION
	fi

	${IMAGE_PREPROCESS_COMMAND}

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

MULTILIBRE_ALLOW_REP =. "${base_bindir}|${base_sbindir}|${bindir}|${sbindir}|${libexecdir}|"
MULTILIB_CHECK_FILE = "${WORKDIR}/multilib_check.py"
MULTILIB_TEMP_ROOTFS = "${WORKDIR}/multilib"

multilib_generate_python_file() {
  cat >${MULTILIB_CHECK_FILE} <<EOF
import sys, os, os.path
import re,filecmp

allow_rep=re.compile(re.sub("\|$","","${MULTILIBRE_ALLOW_REP}"))
error_promt="Multilib check error:"

files={}
dirs=raw_input()
for dir in dirs.split():
  for root, subfolers, subfiles in os.walk(dir):
    for file in subfiles:
      item=os.path.join(root,file)
      key=str(os.path.join("/",os.path.relpath(item,dir)))

      valid=True;
      if files.has_key(key):
        #check whether the file is allow to replace
        if allow_rep.match(key):
          valid=True
        else:
          if not filecmp.cmp(files[key],item):
             valid=False
             print("%s duplicate files %s %s is not the same\n" % (error_promt, item, files[key]))
             sys.exit(1)

      #pass the check, add to list
      if valid:
        files[key]=item
EOF
}

multilib_sanity_check() {
  multilib_generate_python_file
  echo $@ | python ${MULTILIB_CHECK_FILE}
}

get_split_linguas() {
    for translation in ${IMAGE_LINGUAS}; do
        translation_split=$(echo ${translation} | awk -F '-' '{print $1}')
        echo ${translation}
        echo ${translation_split}
    done | sort | uniq
}

rootfs_install_all_locales() {
    # Generate list of installed packages for which additional locale packages might be available
    INSTALLED_PACKAGES=`list_installed_packages | egrep -v -- "(-locale-|^locale-base-|-dev$|-doc$|^kernel|^glibc|^ttf|^task|^perl|^python)"`

    # Generate a list of locale packages that exist
    SPLIT_LINGUAS=`get_split_linguas`
    PACKAGES_TO_INSTALL=""
    for lang in $SPLIT_LINGUAS; do
        for pkg in $INSTALLED_PACKAGES; do
            existing_pkg=`rootfs_check_package_exists $pkg-locale-$lang`
            if [ "$existing_pkg" != "" ]; then
                PACKAGES_TO_INSTALL="$PACKAGES_TO_INSTALL $existing_pkg"
            fi
        done
    done

    # Install the packages, if any
    if [ "$PACKAGES_TO_INSTALL" != "" ]; then
        rootfs_install_packages $PACKAGES_TO_INSTALL
    fi

    # Workaround for broken shell function dependencies
    if false ; then
        get_split_linguas
        list_installed_packages
        rootfs_check_package_exists
    fi
}

# set '*' as the root password so the images
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

	if [ -n "${IMAGE_LINK_NAME}" ]; then
		rm -f ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.manifest
		ln -s ${IMAGE_NAME}.rootfs.manifest ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.manifest
	fi
}

# Make login manager(s) enable automatic login.
# Useful for devices where we do not want to log in at all (e.g. phones)
set_image_autologin () {
        sed -i 's%^AUTOLOGIN=\"false"%AUTOLOGIN="true"%g' ${IMAGE_ROOTFS}/etc/sysconfig/gpelogin
}

# Can be use to create /etc/timestamp during image construction to give a reasonably 
# sane default time setting
rootfs_update_timestamp () {
	date -u +%4Y%2m%2d%2H%2M >${IMAGE_ROOTFS}/etc/timestamp
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
			oe-trim-schemas $schema > $schema.new
        	        mv $schema.new $schema
		fi
	done
}

EXPORT_FUNCTIONS zap_root_password remove_init_link do_rootfs make_zimage_symlink_relative set_image_autologin rootfs_update_timestamp rootfs_no_x_startup

do_fetch[noexec] = "1"
do_unpack[noexec] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"
do_populate_sysroot[noexec] = "1"
do_package[noexec] = "1"
do_package_write_ipk[noexec] = "1"
do_package_write_deb[noexec] = "1"
do_package_write_rpm[noexec] = "1"

addtask rootfs before do_build
