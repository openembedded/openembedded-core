SUMMARY = "XWayland is an X Server that runs under Wayland."
DESCRIPTION = "XWayland is an X Server running as a Wayland client, \
and thus is capable of displaying native X11 client applications in a \
Wayland compositor environment. The goal of XWayland is to facilitate \
the transition from X Window System to Wayland environments, providing \
a way to run unported applications in the meantime."
HOMEPAGE = "https://fedoraproject.org/wiki/Changes/XwaylandStandalone"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=5df87950af51ac2c5822094553ea1880"

SRC_URI = "https://www.x.org/archive/individual/xserver/xwayland-${PV}.tar.xz \
           file://CVE-2023-5367.patch \
           file://CVE-2023-6377.patch \
           file://CVE-2023-6478.patch \
           file://CVE-2023-6816.patch \
           file://CVE-2024-0408.patch \
           file://CVE-2024-0409.patch \
           file://CVE-2023-5380.patch \
           file://CVE-2024-0229-1.patch \
           file://CVE-2024-0229-2.patch \
           file://CVE-2024-0229-3.patch \
           file://CVE-2024-0229-4.patch \
           file://CVE-2024-21885.patch \
           file://CVE-2024-21886-1.patch \
           file://CVE-2024-21886-2.patch \
           file://CVE-2024-31080.patch \
           file://CVE-2024-31081.patch \
           file://CVE-2024-31083-0001.patch \
           file://CVE-2024-31083-0002.patch \
           file://CVE-2024-9632.patch \
           file://CVE-2025-26594-1.patch \
           file://CVE-2025-26594-2.patch \
           file://CVE-2025-26595.patch \
           file://CVE-2025-26596.patch \
           file://CVE-2025-26597.patch \
           file://CVE-2025-26598.patch \
           file://CVE-2025-26599-1.patch \
           file://CVE-2025-26599-2.patch \
           file://CVE-2025-26600.patch \
           file://CVE-2025-26601-1.patch \
           file://CVE-2025-26601-2.patch \
           file://CVE-2025-26601-3.patch \
           file://CVE-2025-26601-4.patch \
           file://CVE-2022-49737.patch \
           file://CVE-2025-49175.patch \
           file://CVE-2025-49176-0001.patch \
           file://CVE-2025-49176-0002.patch \
           file://CVE-2025-49177.patch \
           file://CVE-2025-49178.patch \
           file://CVE-2025-49179.patch \
           file://CVE-2025-49180.patch \
"
SRC_URI[sha256sum] = "d11eeee73290b88ea8da42a7d9350dedfaba856ce4ae44e58c045ad9ecaa2f73"

UPSTREAM_CHECK_REGEX = "xwayland-(?P<pver>\d+(\.(?!90\d)\d+)+)\.tar"

inherit meson features_check pkgconfig
REQUIRED_DISTRO_FEATURES = "x11 opengl"

DEPENDS += "xorgproto xtrans pixman libxkbfile libxfont2 wayland wayland-native wayland-protocols libdrm libepoxy libxcvt"

OPENGL_PKGCONFIGS = "glx glamor dri3"
PACKAGECONFIG ??= "${XORG_CRYPTO} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', '${OPENGL_PKGCONFIGS}', '', d)} \
"
PACKAGECONFIG[dri3] = "-Ddri3=true,-Ddri3=false,libxshmfence"
PACKAGECONFIG[glx] = "-Dglx=true,-Dglx=false,virtual/libgl virtual/libx11"
PACKAGECONFIG[glamor] = "-Dglamor=true,-Dglamor=false,libepoxy virtual/libgbm,libegl"
PACKAGECONFIG[unwind] = "-Dlibunwind=true,-Dlibunwind=false,libunwind"
PACKAGECONFIG[xinerama] = "-Dxinerama=true,-Dxinerama=false"

# Xorg requires a SHA1 implementation, pick one
XORG_CRYPTO ??= "openssl"
PACKAGECONFIG[openssl] = "-Dsha1=libcrypto,,openssl"
PACKAGECONFIG[nettle] = "-Dsha1=libnettle,,nettle"
PACKAGECONFIG[gcrypt] = "-Dsha1=libgcrypt,,libgcrypt"

do_install:append() {
    # remove files not needed and clashing with xserver-xorg
    rm -rf ${D}/${libdir}/xorg/
}

FILES:${PN} += "${libdir}/xorg/protocol.txt"

RDEPENDS:${PN} += "xkbcomp"
