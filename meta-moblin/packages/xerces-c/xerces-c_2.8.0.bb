DESCRIPTION = "Xerces-c is a validating xml parser written in C++"
HOMEPAGE = "http://xerces.apache.org/xerces-c/"
SECTION =  "libs"
PRIORITY = "optional"
LICENSE = "MIT"
PR = "r1"

SRC_URI = "http://mirror.serversupportforum.de/apache/xerces/c/2/sources/xerces-c-src_2_8_0.tar.gz"
S = "${WORKDIR}/xerces-c-src_2_8_0/src/xercesc"

inherit autotools pkgconfig

CCACHE = ""
export XERCESCROOT="${WORKDIR}/xerces-c-src_2_8_0"
export cross_compiling = "yes"

do_configure() {
	./runConfigure -plinux -c"${CC}" -x"${CXX}" -minmem -nsocket -tnative -rpthread -P${D}${prefix} \
                    -C--build=${BUILD_SYS} \
                    -C--host=${HOST_SYS} \
                    -C--target=${TARGET_SYS} \
}

do_compile() {
	${MAKE}
}

do_stage () {
    oe_libinstall -C ${XERCESCROOT}/lib libxerces-c ${STAGING_LIBDIR}
    oe_libinstall -C ${XERCESCROOT}/lib libxerces-depdom ${STAGING_LIBDIR}
    cp -pPR ${XERCESCROOT}/include/xercesc ${STAGING_INCDIR}
}

do_install () {
	${MAKE} install
}
