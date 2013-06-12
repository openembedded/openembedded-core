require cups16.inc

DEPENDS += "libusb \
       ${@base_contains('DISTRO_FEATURES', 'pam', 'libpam', '', d)}"

LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=c5e50cb4b8f24b04636b719683a9102d"
SRC_URI += " \
            file://use_echo_only_in_init.patch \
            file://0001-don-t-try-to-run-generated-binaries.patch \
            file://cups_serverbin.patch \
	"

SRC_URI[md5sum] = "13c8b2b2336d42001abe4899766b62dc"
SRC_URI[sha256sum] = "37a3ebd305e76cfd4c9c53013e89c0f7a4dcb04b2e9da61029a29faa57e0f10d"

EXTRA_OECONF += " --disable-gssapi --enable-debug --disable-relro --enable-libusb \
       ${@base_contains('DISTRO_FEATURES', 'pam', '--enable-pam', '--disable-pam', d)}"

CONFFILES_${PN} += "${sysconfdir}/cups/cupsd.conf"
