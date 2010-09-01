include boost-36.inc

LICENSE = "boost"
LIC_FILES_CHKSUM = "file://LICENSE_1_0.txt;md5=e4224ccaecb14d942c71d31bef20d78c"

PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/boost/${BOOST_P}.tar.bz2 \
           file://arm-intrinsics.patch \
          "

BJAM_OPTS    = '${BJAM_TOOLS} \
                --builddir=${S}/${TARGET_SYS} \
                ${BJAM_EXTRA}'

# build only mt libraries and install symlinks for compatibility
BJAM_EXTRA += "threading=multi"
do_install_append() {
	for lib in ${BOOST_LIBS}; do
	    if [ -e ${D}${libdir}/libboost_${lib}.a ]; then
		ln -s libboost_${lib}.a ${D}${libdir}/libboost_${lib}-mt.a
	    fi
	    if [ -e ${D}${libdir}/libboost_${lib}.so ]; then
		ln -s libboost_${lib}.so ${D}${libdir}/libboost_${lib}-mt.so
	    fi
	done
}
