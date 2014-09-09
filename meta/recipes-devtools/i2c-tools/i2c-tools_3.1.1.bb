SUMMARY = "Set of i2c tools for linux"
SECTION = "base"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI = "http://dl.lm-sensors.org/i2c-tools/releases/${BP}.tar.bz2 \
           file://Module.mk \
"
SRC_URI[md5sum] = "0fdbff53ebd0b8d9249256d6c56480b1"
SRC_URI[sha256sum] = "14d4d7d60d1c12e43f2befe239c682a5c44c27682f153d4b58c1e392d2db1700"

inherit autotools-brokensep

do_compile_prepend() {
    cp ${WORKDIR}/Module.mk ${S}/eepromer/
    sed -i 's#/usr/local#/usr#' ${S}/Makefile
    echo "include eepromer/Module.mk" >> ${S}/Makefile
}

do_install_append() {
    install -d ${D}${includedir}/linux
    install -m 0644 include/linux/i2c-dev.h ${D}${includedir}/linux/i2c-dev-user.h
    rm -f ${D}${includedir}/linux/i2c-dev.h
}

PACKAGES =+ "${PN}-misc"
FILES_${PN}-misc = "${sbindir}/i2c-stub-from-dump \
                        ${bindir}/ddcmon \
                        ${bindir}/decode-edid \
                        ${bindir}/decode-dimms \
                        ${bindir}/decode-vaio \
                       "
RDEPENDS_${PN} += "${PN}-misc"
RDEPENDS_${PN}-misc += "perl"
