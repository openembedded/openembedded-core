SUMMARY = "Online documentation tools"
DESCRIPTION = "A set of documentation tools: man, apropos and whatis"
SECTION = "console/utils"
HOMEPAGE = "http://primates.ximian.com/~flucifredi/man"
LICENSE = "GPLv2"
PR = "r0"

DEPENDS = "groff less"

LIC_FILES_CHKSUM = "file://COPYING;md5=8ca43cbc842c2336e835926c2166c28b"

SRC_URI = "http://primates.ximian.com/~flucifredi/${BPN}/${BPN}-${PV}.tar.gz \
           file://man-1.5k-confpath.patch;striplevel=0 \
           file://man-1.5h1-make.patch; \
           file://man-1.5k-nonascii.patch; \
           file://man-1.6e-security.patch; \
           file://man-1.6e-mandirs.patch; \
           file://man-1.5m2-bug11621.patch; \
           file://man-1.5k-sofix.patch; \
           file://man-1.5m2-buildroot.patch; \
           file://man-1.6e-ro_usr.patch; \
           file://man-1.5i2-newline.patch;striplevel=0 \
           file://man-1.5j-utf8.patch; \
           file://man-1.5i2-overflow.patch; \
           file://man-1.5j-nocache.patch; \
           file://man-1.5i2-initial.patch; \
           file://man-1.5h1-gencat.patch;striplevel=0 \
           file://man-1.5g-nonrootbuild.patch; \
           file://man-1.5m2-tv_fhs.patch;striplevel=0 \
           file://man-1.5j-i18n.patch; \
           file://man-1.6e-whatis2.patch; \
           file://man-1.6e-use_i18n_vars_in_a_std_way.patch; \
           file://man-1.5m2-no-color-for-printing.patch; \
           file://man-1.5m2-sigpipe.patch; \
           file://man-1.6e-i18n_whatis.patch; \
           file://man-1.6e-new_sections.patch; \
           file://man-1.6e-lzma+xz-support.patch; \
           file://man*"

SRC_URI[md5sum] = "67aaaa6df35215e812fd7d89472c44b6"
SRC_URI[sha256sum] = "9f208c7e1981371ad4481d6e6c2c566bc726a15778723f94136d220fb9375f6c"


do_configure () {
        ${S}/configure -default -confdir ${D}/etc +sgid +fhs +lang all
}


fakeroot do_install() {
        oe_runmake install DESTDIR=${D}
}

do_install_append(){
	mkdir -p  ${D}/etc/
        mkdir -p ${D}${datadir}/man/man5
        mkdir -p ${D}${datadir}/man/man7
	cp ${S}/src/man.conf ${D}/etc/
        cp ${WORKDIR}/man.1.gz ${D}${datadir}/man/man1/
        cp ${WORKDIR}/man.7.gz ${D}${datadir}/man/man7/
        cp ${WORKDIR}/manpath.5.gz ${D}${datadir}/man/man5/
}


FILES_${PN} += "${datadir}/locale"
