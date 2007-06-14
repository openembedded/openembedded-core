PROVIDES = "\
    linux-libc-headers \
    virtual/arm-poky-linux-gnueabi-gcc \
    virtual/arm-poky-linux-gnueabi-gcc-initial \
    virtual/arm-poky-linux-binutils \
    virtual/arm-poky-linux-libc-for-gcc \
    virtual/libc \
    virtual/libintl \
    virtual/libiconv \
    glibc-thread-db \
    virtual/linux-libc-headers "
RPROVIDES = "glibc-utils libsegfault glibc-thread-db"
PACKAGES_DYNAMIC = "glibc-gconv-*"
PR = "r1"

inherit sdk

do_stage() {
	if [ ! -e  ${prefix}/package-status ]; then
		echo "The Poky toolchain could not be found in ${prefix}!"
		exit 1
	fi

	install -d ${DEPLOY_DIR}/ipk/
	install -d ${STAGING_DIR}/pkgdata/
	install -d ${STAGING_DIR}/pkgmaps/
	install -d ${STAGING_DIR}/${TARGET_SYS}/shlibs/

	cp -ar ${prefix}/ipk/* ${DEPLOY_DIR}/ipk/
	cp -ar ${prefix}/pkgdata/* ${STAGING_DIR}/pkgdata/
	cp -ar ${prefix}/pkgmaps/* ${STAGING_DIR}/pkgmaps/
	cp -ar ${prefix}/${TARGET_SYS}/shlibs/* ${STAGING_DIR}/${TARGET_SYS}/shlibs/
}

