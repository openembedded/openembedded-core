include boost.inc

LIC_FILES_CHKSUM = "file://LICENSE_1_0.txt;md5=e4224ccaecb14d942c71d31bef20d78c"

PR = "${INC_PR}.0"

SRC_URI += "file://arm-intrinsics.patch"

SRC_URI[md5sum] = "a2dc343f7bc7f83f8941e47ed4a18200"
SRC_URI[sha256sum] = "815a5d9faac4dbd523fbcf3fe1065e443c0bbf43427c44aa423422c6ec4c2e31"

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
