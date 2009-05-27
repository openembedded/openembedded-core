PR = "r3"

SRC_URI = "http://ftp.sudo.ws/sudo/dist/OLD/sudo-1.6.8.tar.gz \
           http://ftp.sudo.ws/sudo/dist/OLD/sudo-1.6.8p12.patch.gz;patch=1 \
           file://nonrootinstall.patch;patch=1 \
           file://nostrip.patch;patch=1 \
           file://autofoo.patch;patch=1 \
           file://noexec-link.patch;patch=1"

require sudo.inc

S = "${WORKDIR}/sudo-1.6.8"
