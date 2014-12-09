
SRC_URI = "http://serf.googlecode.com/svn/src_releases/serf-${PV}.tar.bz2 \
           file://norpath.patch \
           file://env.patch"

SRC_URI[md5sum] = "2e4efe57ff28cb3202a112e90f0c2889"
SRC_URI[sha256sum] = "e0500be065dbbce490449837bb2ab624e46d64fc0b090474d9acaa87c82b2590"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

DEPENDS = "python-scons-native openssl apr apr-util util-linux expat"

do_compile() {
	${STAGING_BINDIR_NATIVE}/scons ${PARALLEL_MAKE} PREFIX=${prefix} \
		CC="${CC}" \
		APR=`which apr-1-config` APU=`which apu-1-config` \
		CFLAGS="${CFLAGS}" LINKFLAGS="${LDFLAGS}" \
		OPENSSL="${STAGING_EXECPREFIXDIR}"
}

do_install() {
	${STAGING_BINDIR_NATIVE}/scons PREFIX=${D}${prefix} LIBDIR=${D}${libdir} install
}

BBCLASSEXTEND = "native"
