require connman.inc
PR = "r1"

EXTRA_OECONF += "\
  ac_cv_path_WPASUPPLICANT=/usr/sbin/wpa_supplicant \
  --disable-gtk-doc \
  --enable-debug \
  --enable-threads \
  --enable-loopback \
  --enable-ethernet \
  ${@base_contains('DISTRO_FEATURES', 'wifi','--enable-wifi', '--disable-wifi', d)} \
  ${@base_contains('DISTRO_FEATURES', 'bluetooth','--enable-bluetooth', '--disable-bluetooth', d)} \
  --enable-dnsproxy \
  --disable-dhclient \
  --enable-test \
  --disable-udev \
  --disable-polkit \
  --enable-client \
  --enable-ofono \
  --prefix=/usr --sysconfdir=/etc --localstatedir=/var"

SRC_URI  = "\
  ${KERNELORG_MIRROR}/linux/network/connman/connman-${PV}.tar.gz \
  file://add_xuser_dbus_permission.patch \
  file://connman \
"

SRC_URI[md5sum] = "9973cb89a11fff6b51fc85b51c13b711"
SRC_URI[sha256sum] = "b15361237f7ec8092fb0e55d4585550ab35491485edaf10ddd032d6e36299db7"
