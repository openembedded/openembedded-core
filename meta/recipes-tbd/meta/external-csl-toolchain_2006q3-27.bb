INHIBIT_DEFAULT_DEPS = "1"

PROVIDES = "\
    linux-libc-headers \
    virtual/arm-none-linux-gnueabi-gcc \
    virtual/arm-none-linux-gnueabi-g++ \
    virtual/arm-none-linux-gnueabi-gcc-initial \
    virtual/arm-none-linux-gnueabi-binutils \
    virtual/arm-none-linux-gnueabi-libc-for-gcc \
    virtual/libc \
    virtual/libintl \
    virtual/libiconv \
    glibc-thread-db \
    virtual/linux-libc-headers "
RPROVIDES = "glibc-utils libsegfault glibc-thread-db"
PACKAGES_DYNAMIC = "glibc-gconv-*"
PR = "r3"

SRC_URI = "http://www.codesourcery.com/public/gnu_toolchain/arm-none-linux-gnueabi/arm-${PV}-arm-none-linux-gnueabi-i686-pc-linux-gnu.tar.bz2 \
file://SUPPORTED"

S = "${WORKDIR}/arm-2006q3"

do_install() {
    install -d ${D}${sysconfdir} ${D}${bindir} ${D}${sbindir} ${D}${base_bindir} ${D}${libdir}
    install -d ${D}${base_libdir} ${D}${base_sbindir} ${D}${datadir}

    cp -a ${S}/arm-none-linux-gnueabi/libc/lib/*  ${D}${base_libdir}
    cp -a ${S}/arm-none-linux-gnueabi/libc/etc/*  ${D}${sysconfdir}
    cp -a ${S}/arm-none-linux-gnueabi/libc/sbin/* ${D}${base_sbindir}
    cp -a ${S}/arm-none-linux-gnueabi/libc/usr/*  ${D}/usr
}

GLIBC_INTERNAL_USE_BINARY_LOCALE ?= "compile"

inherit libc-package

PACKAGES += "libgcc libgcc-dev libstdc++ libstdc++-dev linux-libc-headers"
FILES_libgcc = "${base_libdir}/libgcc_s.so.1"
FILES_libgcc-dev = "${base_libdir}/libgcc_s.so"
FILES_libstdc++ = "${libdir}/libstdc++.so.*"
FILES_libstdc++-dev = "${includedir}/c++/${PV} \
		       ${libdir}/libstdc++.so \
		       ${libdir}/libstdc++.la \
		       ${libdir}/libstdc++.a \
		       ${libdir}/libsupc++.la \
		       ${libdir}/libsupc++.a"
FILES_linux-libc-headers = "${includedir}/asm* \
                            ${includedir}/linux \
                            ${includedir}/mtd \
                            ${includedir}/rdma \
                            ${includedir}/scsi \
                            ${includedir}/sound \
                            ${includedir}/video \
"


