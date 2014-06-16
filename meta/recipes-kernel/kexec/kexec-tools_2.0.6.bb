require kexec-tools.inc
export LDFLAGS = "-L${STAGING_LIBDIR}"
EXTRA_OECONF = " --with-zlib=yes"

SRC_URI += "file://kexec-tools-Refine-kdump-device_tree-sort.patch"

SRC_URI[md5sum] = "c39ea40a7598e49b6dc961ee7de38f57"
SRC_URI[sha256sum] = "a9c6cd9adc8c1c37b3272782f581cb8b4b4070d0e3e921a558a9083f68dcf29a"

PACKAGES =+ "kexec kdump"

FILES_kexec = "${sbindir}/kexec"
FILES_kdump = "${sbindir}/kdump"
