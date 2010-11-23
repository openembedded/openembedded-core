require connman.inc
PR = "r0"

EXTRA_OECONF += "\
  ac_cv_path_WPASUPPLICANT=/usr/sbin/wpa_supplicant \
  --disable-gtk-doc \
  --enable-debug \
  --enable-threads \
  --enable-loopback \
  --enable-ethernet \
  --enable-wifi \
  --enable-bluetooth \
  --enable-dnsproxy \
  --disable-dhclient \
  --enable-test \
  --disable-udev \
  --disable-polkit \
  --enable-client \
  --prefix=/usr --sysconfdir=/etc --localstatedir=/var"

SRC_URI  = "\
  http://www.kernel.org/pub/linux/network/connman/connman-${PV}.tar.gz \
  file://fix-shutdown-ap-disconnect.patch \
  file://connman \
"

SRC_URI[md5sum] = "11616a4fa1d03f96f0739730645f7c53"
SRC_URI[sha256sum] = "e2a57376cdcd42b3876262da6959aa58428941e0eb2df9a4724cd3f1af934492"
