SECTION = "devel"
# Need binutils for libiberty.a
DEPENDS = "elfutils binutils"
SUMMARY = "An ELF prelinking utility"
DESCRIPTION = "The prelink package contains a utility which modifies ELF shared libraries \
and executables, so that far fewer relocations need to be resolved at \
runtime and thus programs come up faster."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=c93c0550bd3173f4504b2cbd8991e50b"
PV = "1.0+git${SRCPV}"
PR = "r0"

SRC_URI = "git://git.pokylinux.org/prelink-cross.git;protocol=git \
           file://prelink.conf \
           file://prelink.cron.daily \
           file://prelink.default"

TARGET_OS_ORIG := "${TARGET_OS}"
OVERRIDES_append = ":${TARGET_OS_ORIG}"

S = "${WORKDIR}/git/trunk"

inherit autotools 

BBCLASSEXTEND = "native"

EXTRA_OECONF = "--disable-selinux --with-pkgversion=${PV}-${PR} \
	--with-bugurl=http://bugzilla.pokylinux.org/"

do_install_append () {
	install -d ${D}${sysconfdir}/cron.daily ${D}${sysconfdir}/default
	install -m 0644 ${WORKDIR}/prelink.conf ${D}${sysconfdir}/prelink.conf
	install -m 0644 ${WORKDIR}/prelink.cron.daily ${D}${sysconfdir}/cron.daily/prelink
	install -m 0644 ${WORKDIR}/prelink.default ${D}${sysconfdir}/default/prelink
}

pkg_postinst_prelink() {
#!/bin/sh

if [ "x$D" != "x" ]; then
  exit 1
fi

prelink -a
}

pkg_postrm_prelink() {
#!/bin/sh

prelink -au
}

