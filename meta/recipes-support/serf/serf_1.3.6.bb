
SRC_URI = "http://serf.googlecode.com/svn/src_releases/serf-1.3.6.tar.bz2 \
           file://norpath.patch"
SRC_URI[md5sum] = "7fe38fa6eab078e0beabf291d8e4995d"
SRC_URI[sha256sum] = "ca637beb0399797d4fc7ffa85e801733cd9c876997fac4a4fd12e9afe86563f2"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

DEPENDS = "python-scons-native openssl apr apr-util util-linux expat"

FULLCC = "${STAGING_BINDIR_TOOLCHAIN}/${CC}"
FULLCC_class-native = "${CC}"

do_compile() {
	${STAGING_BINDIR_NATIVE}/scons ${PARALLEL_MAKE} PREFIX=${prefix} \
		CC="${FULLCC}" \
		APR=`which apr-1-config` APU=`which apu-1-config` \
		CFLAGS="${CFLAGS}" LINKFLAGS="${LDFLAGS}" \
		OPENSSL="${STAGING_EXECPREFIXDIR}"
}

do_install() {
	${STAGING_BINDIR_NATIVE}/scons PREFIX=${D}${prefix} LIBDIR=${D}${libdir} install
}

BBCLASSEXTEND = "native"
