DESCRIPTION = "Linux Bluetooth Stack Userland Utilities."
SECTION = "console"
PRIORITY = "optional"
DEPENDS = "gstreamer gst-plugins-base bluez-libs-${PV} libusb-compat dbus glib-2.0"
PROVIDES = "bluez-utils-dbus"
RPROVIDES_${PN} = "bluez-pan bluez-sdp bluez-utils-dbus"
RREPLACES = "bluez-utils-dbus"
RCONFLICTS_${PN} = "bluez-utils-nodbus"
LICENSE = "GPL"

FILESPATH = "${FILE_DIRNAME}/bluez-utils-${PV}:${FILE_DIRNAME}/bluez-utils"

S = "${WORKDIR}/bluez-utils-${PV}"

inherit autotools update-rc.d

EXTRA_OECONF = " \
                 --enable-bccmd \
		 --enable-hid2hci \
                 --enable-alsa \ 
		 --enable-cups \
		 --enable-glib \
		 --disable-sdpd \
	         --enable-network \
	         --enable-serial \
	         --enable-input \
	         --enable-audio \
	         --enable-echo \
                 --enable-configfile \
	         --enable-initscripts \
		 --enable-test \
		" 

# The config options are explained below:

#  --enable-obex           enable OBEX support
#  --enable-alsa           enable ALSA support, not needed for nokia770, nokia800 and om-gtao1
#  --enable-cups           install CUPS backend support
#  --enable-bccmd          install BCCMD interface utility
#  --enable-avctrl         install Audio/Video control utility
#  --enable-hid2hci        install HID mode switching utility
#  --enable-dfutool        install DFU firmware upgrade utility

#  --enable-glib           For systems that use and install GLib anyway
#  --disable-sdpd          The sdpd is obsolete and should no longer be used. This of course requires that hcid will be started with -s to enable the SDP server

#Following services can be enabled so far:
#	--enable-network
#	--enable-serial
#	--enable-input
#	--enable-audio
#	--enable-echo

#There is no need to modify any init script. They will be started
#automatically or on demand. Only /etc/bluetooth/*.service files should
#be patched to change name or the autostart value.
#	--enable-configfile
#	--enable-initscripts

#For even smaller -doc packages
#	--disable-manpages
#	--disable-pcmciarules

#I haven't seen any embedded device with HID proxy support. So simply
#disable it:
#	--disable-hid2hci

OE_LT_RPATH_ALLOW = "any"
OE_LT_RPATH_ALLOW[export]="1"

do_install_append() {
	install -d ${D}${base_sbindir} ${D}${base_bindir}/  ${D}${sysconfdir}/apm/event.d/
	if [ "${sbindir}" != "${base_sbindir}" ]; then
		mv ${D}${sbindir}/* ${D}${base_sbindir}/
		rmdir ${D}${sbindir}
	fi
	if [ "${bindir}" != "${base_bindir}" ]; then
		mv ${D}${bindir}/* ${D}${base_bindir}/
		rmdir ${D}${bindir}
	fi
	chmod u+s ${D}${base_sbindir}/hciattach ${D}${base_sbindir}/hciconfig
	install -m 0644 ${WORKDIR}/hcid.conf ${D}${sysconfdir}/bluetooth/
        install -m 0644 ${S}/rfcomm/rfcomm.conf ${D}${sysconfdir}/bluetooth/
        install -m 0755 ${S}/daemon/.libs/passkey-agent ${D}${base_bindir}/ ||true 
		install -m 0755 ${S}/test/.libs/passkey-agent ${D}${base_bindir}/ ||true
}


INITSCRIPT_NAME = "bluetooth"
INITSCRIPT_PARAMS = "defaults 23 19"


PACKAGES =+ "${PN}-compat"


FILES_${PN} = " \
               ${base_sbindir}/hcid \
               ${libdir}/bluetooth \
               ${sysconfdir}/init.d/bluetooth \
               ${sysconfdir}/bluetooth/*.service \
               ${sysconfdir}/bluetooth/hcid.conf \
               ${sysconfdir}/default \
               ${sysconfdir}/dbus-1 \
	       ${base_sbindir}/hciattach \
              "

FILES_${PN}-dbg += " \
                   ${libdir}/bluetooth/.debug \
		   ${libdir}/cups/backend/.debug \
		   ${libdir}/alsa-lib/.debug \
		  " 

FILES_${PN}-compat = " \
                    ${base_bindir}/sdptool \
                    ${base_bindir}/dund \
		    ${base_bindir}/rctest \
		    ${base_bindir}/ciptool \
		    ${base_bindir}/l2test \
		    ${base_bindir}/rfcomm \
		    ${base_bindir}/hcitool \
		    ${base_bindir}/pand \
		    ${base_bindir}/hidd \
		    ${base_bindir}/l2ping \
		    ${base_sbindir}/hciconfig \
                    ${base_sbindir}/bccmd \
		    ${base_sbindir}/hciemu \
		    ${base_sbindir}/hid2hci \
		    ${base_bindir}/passkey-agent \
		    ${sysconfdir}/bluetooth/rfcomm.conf \
		   " 











