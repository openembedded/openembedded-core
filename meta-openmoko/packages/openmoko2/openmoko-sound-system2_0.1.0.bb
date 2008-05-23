DESCRIPTION = "Openmoko Sound System"
SECTION = "openmoko/base"
RDEPENDS = "\
  pulseaudio-server \
  pulseaudio-module-alsa-sink \
  pulseaudio-module-alsa-source \
  pulseaudio-module-cli \
  pulseaudio-module-esound-protocol-unix \
  pulseaudio-module-simple-protocol-tcp \
  pulseaudio-module-native-protocol-unix \
  pulseaudio-module-cli-protocol-unix \
  gst-plugin-pulse \
"
RREPLACES = "openmoko-sound-system"
RPROVIDES = "openmoko-sound-system"
PR = "r5"

inherit openmoko-base update-rc.d

INITSCRIPT_NAME = "pulseaudio"
INITSCRIPT_PARAMS = "defaults 35"

SRC_URI = "file://pulseaudio \
           file://session"
S = "${WORKDIR}"

do_install() {
    install -d ${D}/${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/pulseaudio ${D}/${sysconfdir}/init.d/
    install -d ${D}/${sysconfdir}/pulse
	install -m 0755 ${WORKDIR}/session ${D}/${sysconfdir}/pulse/session
}

PACKAGE_ARCH = "all"
