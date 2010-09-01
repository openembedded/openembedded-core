PR = "r0"

SRC_URI = "http://ftp.sudo.ws/sudo/dist/sudo-1.7.2p7.tar.gz \
           file://noexec-link.patch"

require sudo.inc
EXTRA_OECONF += " --with-pam=no"
