require cups16.inc

DEPENDS += "libusb \
       ${@base_contains('DISTRO_FEATURES', 'pam', 'libpam', '', d)}"

LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=c5e50cb4b8f24b04636b719683a9102d"
SRC_URI += " \
            file://use_echo_only_in_init.patch \
            file://0001-don-t-try-to-run-generated-binaries.patch \
            file://cups_serverbin.patch \
	"

SRC_URI[md5sum] = "87ade07e3d1efd03c9c3add949cf9c00"
SRC_URI[sha256sum] = "5842ab1144e653160fe667ee78b932ee036b054c0c2d20533d19e309149a7790"

EXTRA_OECONF += " --disable-gssapi --enable-debug --disable-relro --enable-libusb \
       ${@base_contains('DISTRO_FEATURES', 'pam', '--enable-pam', '--disable-pam', d)}"

CONFFILES_${PN} += "${sysconfdir}/cups/cupsd.conf"
