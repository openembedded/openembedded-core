SUMMARY = "Wayland, a protocol between a compositor and clients"
DESCRIPTION = "Wayland is a protocol for a compositor to talk to its clients \
as well as a C library implementation of that protocol. The compositor can be \
a standalone display server running on Linux kernel modesetting and evdev \
input devices, an X application, or a wayland client itself. The clients can \
be traditional applications, X servers (rootless or fullscreen) or other \
display servers."
HOMEPAGE = "http://wayland.freedesktop.org"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=1d4476a7d98dd5691c53d4d43a510c72 \
                    file://src/wayland-server.c;endline=21;md5=079ae21dbf98ada52ec23744851b0a5c"

SRC_URI = "http://wayland.freedesktop.org/releases/${BPN}-${PV}.tar.xz"
SRC_URI[md5sum] = "1d882776b27329b91d2d500b6d66dd1d"
SRC_URI[sha256sum] = "0069e1e9af888b3e05384380ad8cc6c976ea3e81d08ba19b7675ce1d693a41b5"
SRC_URI_append_class-native = " \
    file://disable-macro-checks-not-used-for-scanner.patch \
    "
EXTRA_OECONF_class-native = "--disable-documentation --enable-scanner"

inherit autotools pkgconfig

# We need wayland-native for the wayland-scanner utility
BBCLASSEXTEND = "native"

DEPENDS_class-native = "expat-native libffi-native"
DEPENDS = "expat libffi wayland-native"

EXTRA_OECONF = "--disable-documentation --disable-scanner"

# Wayland installs a M4 macro for other projects to use, which uses the target
# pkg-config to find files.  Replace pkg-config with pkg-config-native.
do_install_append_class-native() {
  sed -e 's,PKG_CHECK_MODULES(.*),,g' \
      -e 's,$PKG_CONFIG,pkg-config-native,g' \
      -i ${D}/${datadir}/aclocal/wayland-scanner.m4
}

sysroot_stage_all_append_class-target () {
	cp ${STAGING_DATADIR_NATIVE}/aclocal/wayland-scanner.m4 ${SYSROOT_DESTDIR}/${datadir}/aclocal/
}
