require kexec-tools.inc
export LDFLAGS = "-L${STAGING_LIBDIR}"
EXTRA_OECONF = " --with-zlib=yes"

SRC_URI += "file://kexec-tools-Refine-kdump-device_tree-sort.patch \
            file://kexec-aarch64.patch \
            file://kexec-x32.patch \
         "

SRC_URI[md5sum] = "4ecb7ab7ad9eb6ce413899bdb07a8426"
SRC_URI[sha256sum] = "c2c6d204fe0911ebd304c40100163237feca4c5a854a2cca382ee36916a573d8"

PACKAGES =+ "kexec kdump vmcore-dmesg"

ALLOW_EMPTY_${PN} = "1"
RRECOMMENDS_${PN} = "kexec kdump vmcore-dmesg"

FILES_kexec = "${sbindir}/kexec"
FILES_kdump = "${sbindir}/kdump"
FILES_vmcore-dmesg = "${sbindir}/vmcore-dmesg"
