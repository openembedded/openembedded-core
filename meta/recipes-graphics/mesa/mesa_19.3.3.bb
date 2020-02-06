require ${BPN}.inc

SRC_URI = "https://mesa.freedesktop.org/archive/mesa-${PV}.tar.xz \
           file://0001-meson.build-check-for-all-linux-host_os-combinations.patch \
           file://0002-meson.build-make-TLS-ELF-optional.patch \
           file://0003-Allow-enable-DRI-without-DRI-drivers.patch \
           file://0004-Revert-mesa-Enable-asm-unconditionally-now-that-gen_.patch \
           file://0005-vc4-use-intmax_t-for-formatted-output-of-timespec-me.patch \
           file://0001-meson-misdetects-64bit-atomics-on-mips-clang.patch \
           "

SRC_URI[md5sum] = "00010e0bb8f6641276ff6cb3e9386114"
SRC_URI[sha256sum] = "81ce4810bb25d61300f8104856461f4d49cf7cb794aa70cb572312e370c39f09"

UPSTREAM_CHECK_GITTAGREGEX = "mesa-(?P<pver>\d+(\.\d+)+)"

CFLAGS += "-fcommon"

#because we cannot rely on the fact that all apps will use pkgconfig,
#make eglplatform.h independent of MESA_EGL_NO_X11_HEADER
do_install_append() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'egl', 'true', 'false', d)}; then
        sed -i -e 's/^#elif defined(__unix__) && defined(EGL_NO_X11)$/#elif defined(__unix__) \&\& defined(EGL_NO_X11) || ${@bb.utils.contains('PACKAGECONFIG', 'x11', '0', '1', d)}/' ${D}${includedir}/EGL/eglplatform.h
    fi
}
