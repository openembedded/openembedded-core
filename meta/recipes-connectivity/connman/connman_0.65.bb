require connman.inc
PR = "r1"

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
  ${KERNELORG_MIRROR}/linux/network/connman/connman-${PV}.tar.gz \
  file://fix-shutdown-ap-disconnect.patch \
  file://add_xuser_dbus_permission.patch \
  file://connman \
"

SRC_URI[md5sum] = "bd714da295ed2d2d91a49539f4c4fa3a"
SRC_URI[sha256sum] = "a1c1d93da6bb4c2d8ae53293b06f237e02f5e796d2bba73ec639a466d05259c3"
