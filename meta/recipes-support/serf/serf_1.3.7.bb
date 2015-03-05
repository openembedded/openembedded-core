
SRC_URI = "http://serf.googlecode.com/svn/src_releases/serf-1.3.7.tar.bz2 \
           file://norpath.patch \
           file://env.patch"
SRC_URI[md5sum] = "0a6fa745df4517dd8f79c75c538919bc"
SRC_URI[sha256sum] = "ecccb74e665e6ea7539271e126a21d0f7eeddfeaa8ce090adb3aec6682f9f0ae"

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
