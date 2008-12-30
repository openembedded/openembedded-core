SRC_URI = "http://archive.ubuntu.com/ubuntu/pool/main/l/linux-ubuntu-modules-2.6.24/linux-ubuntu-modules-2.6.24_2.6.24-22.35.tar.gz \
           file://menlow-config"

S = "${WORKDIR}/lum/ubuntu"

inherit module

export src="${S}"

do_configure_prepend () {
	cp ${WORKDIR}/menlow-config ${S}/.config
}

do_compile() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	make -C ${STAGING_KERNEL_DIR} ARCH=i386 M=${S} modules
}

MODULE_PATH = "/lib/modules/${KERNEL_VERSION}"

do_install () {
        install -d ${D}${MODULE_PATH}/kernel/drivers/char/drm/
	install -m 644 ${S}/media/drm-poulsbo/drm.ko ${D}${MODULE_PATH}/kernel/drivers/char/drm/
	install -m 644 ${S}/media/drm-poulsbo/psb.ko ${D}${MODULE_PATH}/kernel/drivers/char/drm/
}

COMPATIBLE_MACHINE = "menlow"					