DESCRIPTION = "ALSA Utilities"
HOMEPAGE = "http://www.alsa-project.org"
BUGTRACKER = "https://bugtrack.alsa-project.org/alsa-bug/login_page.php"
SECTION = "console/utils"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
                    file://alsactl/utils.c;beginline=1;endline=20;md5=fe9526b055e246b5558809a5ae25c0b9"
DEPENDS = "alsa-lib ncurses libsamplerate0 udev"

SRC_URI = "ftp://ftp.alsa-project.org/pub/utils/alsa-utils-${PV}.tar.bz2 \
           file://0001-alsactl-don-t-let-systemd-unit-restore-the-volume-wh.patch \
          "

SRC_URI[md5sum] = "b65e9a04181bd7c9db7667a4566f8dc3"
SRC_URI[sha256sum] = "02bfac39092f3b68d743c23ad3d688d6c5aa8df69f2ccd692c5b8282edb352ea"

# lazy hack. needs proper fixing in gettext.m4, see
# http://bugs.openembedded.org/show_bug.cgi?id=2348
# please close bug and remove this comment when properly fixed
#
EXTRA_OECONF = "--disable-xmlto --with-udev-rules-dir=`pkg-config --variable=udevdir udev`/rules.d "
EXTRA_OECONF_append_libc-uclibc = " --disable-nls"

inherit autotools gettext

# This are all packages that we need to make. Also, the now empty alsa-utils
# ipk depends on them.

ALSA_UTILS_PKGS = "\
             alsa-utils-alsamixer \
             alsa-utils-midi \
             alsa-utils-aplay \
             alsa-utils-amixer \
             alsa-utils-aconnect \
             alsa-utils-iecset \
             alsa-utils-speakertest \
             alsa-utils-aseqnet \
             alsa-utils-aseqdump \
             alsa-utils-alsactl \
             alsa-utils-alsaloop \
             alsa-utils-alsaucm \
            "

PACKAGES += "${ALSA_UTILS_PKGS}"
RDEPENDS_${PN} += "${ALSA_UTILS_PKGS}"

FILES_${PN} = ""
FILES_alsa-utils-aplay       = "${bindir}/aplay ${bindir}/arecord"
FILES_alsa-utils-amixer      = "${bindir}/amixer"
FILES_alsa-utils-alsamixer   = "${bindir}/alsamixer"
FILES_alsa-utils-speakertest = "${bindir}/speaker-test ${datadir}/sounds/alsa/ ${datadir}/alsa/speaker-test/"
FILES_alsa-utils-midi        = "${bindir}/aplaymidi ${bindir}/arecordmidi ${bindir}/amidi"
FILES_alsa-utils-aconnect    = "${bindir}/aconnect"
FILES_alsa-utils-aseqnet     = "${bindir}/aseqnet"
FILES_alsa-utils-iecset      = "${bindir}/iecset"
FILES_alsa-utils-alsactl     = "${sbindir}/alsactl */udev/rules.d ${systemd_unitdir} ${localstatedir}/lib/alsa ${datadir}/alsa/init/"
FILES_alsa-utils-aseqdump    = "${bindir}/aseqdump"
FILES_alsa-utils-alsaloop    = "${bindir}/alsaloop"
FILES_alsa-utils-alsaucm     = "${bindir}/alsaucm"


DESCRIPTION_alsa-utils-aplay        = "play (and record) sound files via ALSA"
DESCRIPTION_alsa-utils-amixer       = "command-line based control for ALSA mixer and settings"
DESCRIPTION_alsa-utils-alsamixer    = "ncurses based control for ALSA mixer and settings"
DESCRIPTION_alsa-utils-speakertest  = "ALSA surround speaker test utility"
DESCRIPTION_alsa-utils-midi         = "miscalleanous MIDI utilities for ALSA"
DESCRIPTION_alsa-utils-aconnect     = "ALSA sequencer connection manager"
DESCRIPTION_alsa-utils-aseqnet      = "network client/server on ALSA sequencer"
DESCRIPTION_alsa-utils-alsactl      = "saves/restores ALSA-settings in /etc/asound.state"
DESCRIPTION_alsa-utils-alsaucm      = "ALSA Use Case Manager"

RRECOMMENDS_alsa-utils-alsactl = "alsa-states"

ALLOW_EMPTY_alsa-utils = "1"

do_install() {
	autotools_do_install

	# We don't ship this here because it requires a dependency on bash.
	# See alsa-utils-alsaconf_${PV}.bb
	rm ${D}${sbindir}/alsaconf
}
