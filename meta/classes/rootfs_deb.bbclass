DEPENDS_prepend = "dpkg-native apt-native fakeroot-native "
DEPENDS_append = " ${EXTRA_IMAGEDEPENDS}"

PACKAGES = ""

do_rootfs[nostamp] = 1
do_rootfs[dirs] = ${TOPDIR}
do_build[nostamp] = 1

ROOTFS_POSTPROCESS_COMMAND ?= ""

PID = "${@os.getpid()}"

# some default locales
IMAGE_LINGUAS ?= "de-de fr-fr en-gb"

LINGUAS_INSTALL = "${@" ".join(map(lambda s: "locale-base-%s" % s, bb.data.getVar('IMAGE_LINGUAS', d, 1).split()))}"

fakeroot rootfs_deb_do_rootfs () {
	set +e
	mkdir -p ${IMAGE_ROOTFS}/var/dpkg/{info,updates}

	rm -f ${STAGING_DIR}/etc/apt/sources.list
	rm -f ${STAGING_DIR}/etc/apt/preferences
	> ${IMAGE_ROOTFS}/var/dpkg/status
	> ${IMAGE_ROOTFS}/var/dpkg/available
	# > ${STAGING_DIR}/var/dpkg/status

	priority=1
	for arch in ${PACKAGE_ARCHS}; do
		if [ ! -d ${DEPLOY_DIR_DEB}/$arch ]; then
			continue;
		fi
		cd ${DEPLOY_DIR_DEB}/$arch
		# if [ -z "${DEPLOY_KEEP_PACKAGES}" ]; then
			rm -f Packages.gz Packages Packages.bz2
		# fi
		apt-ftparchive packages . | bzip2 > Packages.bz2

		echo "deb file:${DEPLOY_DIR_DEB}/$arch/ ./" >> ${STAGING_DIR}/etc/apt/sources.list
		(echo "Package: *"
		echo "Pin origin ${DEPLOY_DIR_DEB}/$arch"
		echo "Pin-Priority: $((800 + $priority))") >> ${STAGING_DIR}/etc/apt/preferences
		priority=$(expr $priority + 5)
	done

	export APT_CONFIG="${STAGING_DIR}/etc/apt/apt.conf"
	export D=${IMAGE_ROOTFS}
	export OFFLINE_ROOT=${IMAGE_ROOTFS}
	export IPKG_OFFLINE_ROOT=${IMAGE_ROOTFS}

	apt-get update

	_flag () {
		sed -i -e "/^Package: $2\$/{n; s/Status: install ok .*/Status: install ok $1/;}" ${IMAGE_ROOTFS}/var/dpkg/status
	}
	_getflag () {
		cat ${IMAGE_ROOTFS}/var/dpkg/status | sed -n -e "/^Package: $2\$/{n; s/Status: install ok .*/$1/; p}"
	}

	if [ ! -z "${LINGUAS_INSTALL}" ]; then
		apt-get install glibc-localedata-i18n
		if [ $? -eq 1 ]; then
			exit 1
		fi
		for i in ${LINGUAS_INSTALL}; do
			apt-get install $i
			if [ $? -eq 1 ]; then
				exit 1
			fi
		done
	fi

	if [ ! -z "${PACKAGE_INSTALL}" ]; then
		for i in ${PACKAGE_INSTALL}; do
			apt-get install $i
			if [ $? -eq 1 ]; then
				exit 1
			fi
			find ${IMAGE_ROOTFS} -name \*.dpkg-new | for i in `cat`; do
				mv $i `echo $i | sed -e's,\.dpkg-new$,,'`
			done
		done
	fi

	install -d ${IMAGE_ROOTFS}/${sysconfdir}
	echo ${BUILDNAME} > ${IMAGE_ROOTFS}/${sysconfdir}/version

	for i in ${IMAGE_ROOTFS}/var/dpkg/info/*.preinst; do
		if [ -f $i ] && ! sh $i; then
			_flag unpacked `basename $i .preinst`
		fi
	done

	for i in ${IMAGE_ROOTFS}/var/dpkg/info/*.postinst; do
		if [ -f $i ] && ! sh $i configure; then
			_flag unpacked `basename $i .postinst`
		fi
	done

	set -e

	${ROOTFS_POSTPROCESS_COMMAND}
}

# set '*' as the rootpassword so the images
# can decide if they want it or not

zap_root_password () {
	sed 's%^root:[^:]*:%root:*:%' < ${IMAGE_ROOTFS}/etc/passwd >${IMAGE_ROOTFS}/etc/passwd.new
	mv ${IMAGE_ROOTFS}/etc/passwd.new ${IMAGE_ROOTFS}/etc/passwd	
} 

create_etc_timestamp() {
	date +%2m%2d%2H%2M%Y >${IMAGE_ROOTFS}/etc/timestamp
}

# Turn any symbolic /sbin/init link into a file
remove_init_link () {
	if [ -h ${IMAGE_ROOTFS}/sbin/init ]; then
		LINKFILE=${IMAGE_ROOTFS}`readlink ${IMAGE_ROOTFS}/sbin/init`
		rm ${IMAGE_ROOTFS}/sbin/init
		cp $LINKFILE ${IMAGE_ROOTFS}/sbin/init
	fi
}

# export the zap_root_password, create_etc_timestamp and remote_init_link
EXPORT_FUNCTIONS zap_root_password create_etc_timestamp remove_init_link do_rootfs

addtask rootfs before do_build after do_install
