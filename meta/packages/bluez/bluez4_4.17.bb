require bluez4.inc

DEPENDS += "glib-2.0"
PR = "r4"

PACKAGES =+ "${PN}-compat"

CONFFILES_${PN} = " \
                   ${sysconfdir}/bluetooth/main.conf \
                  "

CONFFILES_${PN}-compat = " \
                          ${sysconfdir}/bluetooth/rfcomm.conf \
			 "

FILES_${PN} = " \
               ${base_sbindir}/bluetoothd \
               ${libdir}/bluetooth \
               ${sysconfdir}/init.d/bluetooth \
               ${sysconfdir}/bluetooth/*.service \
               ${sysconfdir}/bluetooth/main.conf \
               ${sysconfdir}/default \
               ${sysconfdir}/dbus-1 \
	       ${base_sbindir}/hciattach \
	       ${base_sbindir}/hid2hci \
              "

FILES_${PN}-dbg += " \
                   ${libdir}/bluetooth/.debug \
		   ${libdir}/cups/backend/.debug \
		   ${libdir}/alsa-lib/.debug \
		  "

FILES_${PN}-lib-dbg += " ${libdir}/bluetooth/plugins/.debug/*.so"


FILES_${PN}-compat = " \
                    ${base_sbindir}/bccmd \
                    ${base_sbindir}/hciconfig \
                    ${base_bindir}/ciptool \
                    ${base_bindir}/dfutool \
                    ${base_bindir}/dund \
                    ${base_bindir}/hcitool \
                    ${base_bindir}/hidd \
                    ${base_bindir}/l2ping \
                    ${base_bindir}/pand \
                    ${base_bindir}/rfcomm \
                    ${base_bindir}/sdptool \
		    ${sysconfdir}/bluetooth/rfcomm.conf \
		   "
