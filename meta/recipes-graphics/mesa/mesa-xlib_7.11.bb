include mesa-common.inc

SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/${PV}/MesaLib-${PV}.tar.bz2 \
           file://uclibc.patch \
           "

SRC_URI[md5sum] = "ff03aca82d0560009a076a87c888cf13"
SRC_URI[sha256sum] = "f8bf37a00882840a3e3d327576bc26a79ae7f4e18fe1f7d5f17a5b1c80dd7acf"

LIC_FILES_CHKSUM = "file://docs/license.html;md5=7a3373c039b6b925c427755a4f779c1d"

PROTO_DEPS = "xf86driproto glproto"
LIB_DEPS = "virtual/libx11 libxext libxxf86vm libxdamage libxfixes libxml2-native"

DEPENDS = "${PROTO_DEPS}  ${LIB_DEPS} makedepend-native"

PE = "1"
PR = "r1"

EXTRA_OECONF += "--with-driver=xlib --without-gallium-drivers"

do_configure_prepend() {
	#check for python not python2, because python-native does not stage python2 binary/link
	sed -i 's/AC_CHECK_PROGS(\[PYTHON2\], \[python2 python\])/AC_CHECK_PROGS(\[PYTHON2\], \[python python\])/g' ${S}/configure.ac
	# We need builtin_compiler built for buildhost arch instead of target (is provided by mesa-dri-glsl-native)"
	sed -i "s#\./builtin_compiler#${STAGING_BINDIR_NATIVE}/glsl/builtin_compiler#g" ${S}/src/glsl/Makefile
}

