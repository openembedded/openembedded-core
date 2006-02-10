DESCRIPTION = "ALSA Utilities"
MAINTAINER = "Lorn Potter <lpotter@trolltech.com>"
SECTION = "console/utils"
LICENSE = "GPL"
DEPENDS = "alsa-lib ncurses"

SRC_URI = "ftp://ftp.alsa-project.org/pub/utils/alsa-utils-${PV}.tar.bz2"

inherit autotools

# This are all packages that we need to make. Also, the now empty alsa-utils
# ipk depend on them.

PACKAGES += "alsa-utils-alsamixer"
PACKAGES += "alsa-utils-midi"
PACKAGES += "alsa-utils-aplay"
PACKAGES += "alsa-utils-amixer"
PACKAGES += "alsa-utils-aconnect"
PACKAGES += "alsa-utils-iecset"
PACKAGES += "alsa-utils-speakertest"
PACKAGES += "alsa-utils-aseqnet"
PACKAGES += "alsa-utils-alsactl"


# We omit alsaconf, because
# a) this is a bash script
# b) it creates config files for RedHat, Debian, Mandrake etc, but not
#    for Familiar, OpenZaurus etc


FILES_${PN} = ""
FILES_alsa-utils-aplay       = "${bindir}/aplay ${bindir}/arecord"
FILES_alsa-utils-amixer      = "${bindir}/amixer"
FILES_alsa-utils-alsamixer   = "${bindir}/alsamixer"
FILES_alsa-utils-speakertest = "${bindir}/speaker-test"
FILES_alsa-utils-midi        = "${bindir}/aplaymidi ${bindir}/arecordmidi ${bindir}/amidi"
FILES_alsa-utils-aconnect    = "${bindir}/aconnect"
FILES_alsa-utils-aseqnet     = "${bindir}/aseqnet"
FILES_alsa-utils-iecset      = "${bindir}/iecset"
FILES_alsa-utils-alsactl     = "${sbindir}/alsactl"

DESCRIPTION_alsa-utils-aplay        = "play (and record) sound files via ALSA"
DESCRIPTION_alsa-utils-amixer       = "command-line based control for ALSA mixer and settings"
DESCRIPTION_alsa-utils-alsamixer    = "ncurses based control for ALSA mixer and settings"
#DESCRIPTION_alsa-utils-speaker-test= "??"
DESCRIPTION_alsa-utils-midi         = "miscalleanous MIDI utilities for ALSA"
DESCRIPTION_alsa-utils-aconnect     = "ALSA sequencer connection manager"
DESCRIPTION_alsa-utils-aseqnet      = "network client/server on ALSA sequencer"
DESCRIPTION_alsa-utils-alsactl      = "saves/restores ALSA-settings in /etc/asound.state"
DESCRIPTION_alsa-utils-alsaconf     = "a bash script that creates ALSA configuration files"

RDEPENDS_alsa-utils-aplay  += "alsa-conf"
RDEPENDS_alsa-utils-amixer += "alsa-conf"

ALLOW_EMPTY_alsa-utils = "1"
