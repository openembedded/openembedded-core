require cups16.inc

DEPENDS += "libusb \
       ${@base_contains('DISTRO_FEATURES', 'pam', 'libpam', '', d)}"

LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=c5e50cb4b8f24b04636b719683a9102d"
SRC_URI += " \
            file://use_echo_only_in_init.patch \
            file://0001-don-t-try-to-run-generated-binaries.patch \
            file://cups_serverbin.patch \
	"

SRC_URI[md5sum] = "946a2d8ad1aec6beced312fce18543da"
SRC_URI[sha256sum] = "84fa83dea6ed08dbd4d1112d9b0005424713b32bcb13111857836312896cf29d"

EXTRA_OECONF += " --disable-gssapi --enable-debug --disable-relro --enable-libusb \
       ${@base_contains('DISTRO_FEATURES', 'pam', '--enable-pam', '--disable-pam', d)}"

CONFFILES_${PN} += "${sysconfdir}/cups/cupsd.conf"
