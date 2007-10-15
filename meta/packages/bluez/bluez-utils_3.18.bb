require bluez-utils.inc

DEPENDS += "glib-2.0"
PR = "r3"

# ti patch doesn't apply, people using it should rediff it and send it upstream
#SRC_URI += "file://hciattach-ti-bts.patch;patch=1 "

SRC_URI += "file://allow-no-hal.patch;patch=1 "

EXTRA_OECONF = " \
                 --enable-bccmd \
		 --enable-hid2hci \
                 --enable-alsa \ 
		 --disable-cups \
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
		 --disable-hal \
		"

# The config options are explained below:

#  --enable-obex           enable OBEX support
#  --enable-alsa           enable ALSA support, not needed for nokia770, nokia800 and fic-gtao1
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


PACKAGES =+ "${PN}-compat"

CONFFILES_${PN} = " \
                   ${sysconfdir}/bluetooth/hcid.conf \
                   ${sysconfdir}/default/bluetooth \
                  "

CONFFILES_${PN}-compat = " \
                          ${sysconfdir}/bluetooth/rfcomm.conf \
			 "

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

