DESCRIPTION = "open source processor emulator"
HOMEPAGE = "http://qemu.org"
LICENSE = "GPLv2 & LGPLv2.1"
DEPENDS = "glib-2.0 zlib alsa-lib virtual/libx11"

# QEMU_TARGETS is overridable variable
QEMU_TARGETS ?= "arm i386 mips mipsel mips64 mips64el ppc sh4 x86_64"

require qemu-targets.inc

SDL ?= "--disable-sdl"
SDL_virtclass-native ?= ""
SDL_virtclass-nativesdk ?= ""

EXTRA_OECONF += "--target-list=${@get_qemu_target_list(d)} --disable-werror --disable-vnc-tls --audio-drv-list=oss,alsa --audio-card-list=ac97,es1370 ${SDL} --disable-curl --disable-vnc-jpeg"

#EXTRA_OECONF += "--disable-sdl"


inherit autotools

do_configure() {
    # Handle distros such as CentOS 5 32-bit that do not have kvm support
    KVMOPTS="--disable-kvm"
    if [ "${PN}" != "qemu-native" -a "${PN}" != "qemu-nativesdk" ] \
       || [ -f /usr/include/linux/kvm.h ] ; then
       KVMOPTS="--enable-kvm"
    fi

    ${S}/configure --prefix=${prefix} --sysconfdir=${sysconfdir} --disable-strip ${EXTRA_OECONF} $KVMOPTS
    test ! -e ${S}/target-i386/beginend_funcs.sh || chmod a+x ${S}/target-i386/beginend_funcs.sh
}

do_install () {
	export STRIP="true"
	autotools_do_install
	install -d ${D}${datadir}/qemu
	install -m 0755 ${WORKDIR}/powerpc_rom.bin ${D}${datadir}/qemu
}

PACKAGECONFIG ??= "gl"
PACKAGECONFIG[gl] = "--enable-gl-accel,--disable-gl-accel,,"

DEPENDS_virtclass-native = "zlib-native alsa-lib-native glib-2.0-native"
DEPENDS_virtclass-nativesdk = "zlib-nativesdk libsdl-nativesdk glib-2.0-nativesdk \
                               ${@base_contains('DISTRO_FEATURES', 'x11', 'qemugl-nativesdk', '', d)}"
RDEPENDS_virtclass-nativesdk = "libsdl-nativesdk"
EXTRA_OECONF_virtclass-nativesdk = "--target-list=${@get_qemu_target_list(d)} --disable-werror --disable-vnc-tls \
				--disable-curl \
				"

BBCLASSEXTEND = "native nativesdk"

# Qemu target will not build in world build for ARM or Mips
BROKEN_qemuarm = "1"
BROKEN_qemumips = "1"

INSANE_SKIP_${PN} = "arch"
