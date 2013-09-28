require cups16.inc

DEPENDS += "libusb \
       ${@base_contains('DISTRO_FEATURES', 'pam', 'libpam', '', d)}"

LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=c5e50cb4b8f24b04636b719683a9102d"
SRC_URI += " \
            file://use_echo_only_in_init.patch \
            file://0001-don-t-try-to-run-generated-binaries.patch \
            file://cups_serverbin.patch \
	"

SRC_URI[md5sum] = "52c3df269709a4d25472cfe72ab5e856"
SRC_URI[sha256sum] = "b910c3e43b2d06f54154d12710e849455d0710c9983dab0a5f49a5e93b1b7e73"

EXTRA_OECONF += " --disable-gssapi --enable-debug --disable-relro --enable-libusb \
       ${@base_contains('DISTRO_FEATURES', 'pam', '--enable-pam', '--disable-pam', d)}"

CONFFILES_${PN} += "${sysconfdir}/cups/cupsd.conf"
