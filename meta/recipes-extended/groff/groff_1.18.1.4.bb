DESCRIPTION = "groff-a short reference for the GNU roff language "
SECTION = "console/utils"
PRIORITY = "required"
HOMEPAGE = "ftp://ftp.gnu.org/gnu/groff/"
LICENSE = "GPLv2"
PR = "r0"

LIC_FILE_CHKSUM = "file://CORYING;md5=e43fc16fccd8519fba405f0a0ff6e8a3"

SRC_URI = "ftp://ftp.gnu.org/gnu/groff/groff-1.18.1.4.tar.gz \
          file://groff-1.18.1.4-remove-mom.patch;striplevel=1 \
          file://man-local.patch;patch=1 \
          file://mdoc-local.patch;patch=1" 

inherit autotools

EXTRA_OEMAKE =+ "-f Makefile mandir=${D}${mandir}"

localdir=/usr/local/
do_configure (){
	mkdir -p ${D}/usr/local  
        ${S}/configure --prefix=${D}/usr/local
}


fakeroot do_install(){
        mkdir -p ${D}/usr/local/man/man1
        mkdir -p ${D}/usr/local/man/man5
        mkdir -p ${D}/usr/local/man/man7
        oe_runmake install DESTDIR=${D}
}

do_install_append() {
    mv ${D}/usr/local/bin ${D}/usr
    mkdir -p ${D}${sysconfdir}/groff
    cp -rf ${D}/usr/local/share/groff/site-tmac/* ${D}/usr/local/share/groff/1.18.1.4/tmac/
    cp -rf ${D}/usr/local/share/groff/site-tmac/* ${D}${sysconfdir}/groff/
}

pkg_postinst_${PN}() {
    ln -s ${bindir}/tbl ${bindir}/gtbl
   echo "export GROFF_FONT_PATH=/usr/local/share/groff/1.18.1.4/font" >> ${sysconfdir}/profile
    echo "export GROFF_TMAC_PATH=/usr/local/share/groff/1.18.1.4/tmac" >> ${sysconfdir}/profile
}

FILES_${PN} += "${localdir}/"
