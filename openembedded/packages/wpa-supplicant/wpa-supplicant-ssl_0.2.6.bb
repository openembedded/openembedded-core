DESCRIPTION = "User space helper for WPA and WPA2 client operations with SSL. \
The Supplicant is used in WPA/WPA2 key handshakes to authenticate \
with the AP and to generate dynamic encryption keys (TKIP or CCMP)."
DEPENDSS = "openssl"
PR = "r3"

EXTRA_OEMAKE="LIBS='-L${STAGING_LIBDIR} -lssl -lcrypto' LIBS_p='-L${STAGING_LIBDIR} -lssl -lcrypto'"

include wpa-supplicant_${PV}.inc
