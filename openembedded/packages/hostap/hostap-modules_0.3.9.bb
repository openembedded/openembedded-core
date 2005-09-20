DESCRIPTION = "A driver for wireless LAN cards based on Intersil's Prism2/2.5/3 chipset"
SECTION = "kernel/modules"
PRIORITY = "optional"
MAINTAINER = "Michael 'Mickey' Lauer <mickey@Vanille.de>"
LICENSE = "GPL"
PR = "r4"

SRC_URI = "http://hostap.epitest.fi/releases/hostap-driver-${PV}.tar.gz \
           file://hostap_cs.conf \
	   file://Makefile.patch;patch=1 \
	   file://add_event.patch;patch=1 \
	   file://hostap-utsname.patch;patch=1 \
	   file://hostap_cardid.patch;patch=1"
SRC_URI_append_mtx-1 = " file://mtx_compat.diff;patch=1;pnum=0 \
	file://mtx_hostap_deferred_irq.diff;patch=1;pnum=0"
SRC_URI_append_h3900 = " file://ipaq_compat.patch;patch=1 "

S = "${WORKDIR}/hostap-driver-${PV}"

inherit module

# Hack Alert :D
ARCH_mipsel = "mips"
MAKE_TARGETS = "KERNEL_PATH=${STAGING_KERNEL_DIR} MAKE='make -e'"

NET_MODULES = "hostap hostap_pci hostap_crypt_ccmp hostap_crypt_tkip hostap_crypt_wep"

do_install() {
	install -d ${D}${base_libdir}/modules/${KERNEL_VERSION}/net \
		   ${D}${base_libdir}/modules/${KERNEL_VERSION}/pcmcia \
        	   ${D}${sysconfdir}/pcmcia
	for i in ${NET_MODULES}
	do
		install -m 0644 driver/modules/$i${KERNEL_OBJECT_SUFFIX} ${D}${base_libdir}/modules/${KERNEL_VERSION}/net/
	done
	install -m 0644 driver/modules/hostap_cs${KERNEL_OBJECT_SUFFIX} ${D}${base_libdir}/modules/${KERNEL_VERSION}/pcmcia/
	install -m 0644 driver/etc/hostap_cs.conf ${D}${sysconfdir}/pcmcia/hostap_cs.conf
	cat ${WORKDIR}/hostap_cs.conf >>${D}${sysconfdir}/pcmcia/hostap_cs.conf
}

PACKAGES = "hostap-modules-cs hostap-modules-pci hostap-modules"
FILES_hostap-modules-cs = "/lib/modules/${KERNEL_VERSION}/pcmcia/ /${sysconfdir}/pcmcia/"
FILES_hostap-modules-pci = "/lib/modules/${KERNEL_VERSION}/net/hostap_pci${KERNEL_OBJECT_SUFFIX}"
FILES_hostap-modules = "/lib/modules/"
RDEPENDS_hostap-modules-cs = "hostap-modules"
RDEPENDS_hostap-modules-pci = "hostap-modules"
