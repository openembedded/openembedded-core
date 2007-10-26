#!/bin/sh

CARD_ID=`/usr/sbin/hostap_diag $INTERFACE|grep NICID|awk '{print $2}'|sed -e 's/id=0x//'`

# 801d cards lack even Primary firmware so we cannot use hostap_diag
PRI=/lib/firmware/pm010102.hex 
STA=/lib/firmware/rf010704.hex

if [ $CARD_ID = '800c' ] || [ $CARD_ID = '8013' ] || [ $CARD_ID = '8017' ] || \
   [ $CARD_ID = '801b' ] || [ $CARD_ID = '8022' ] || [ $CARD_ID = '8023' ] ; then
        PRI=/lib/firmware/ak010104.hex 
elif [ $CARD_ID = '800b' ] || [ $CARD_ID = '8012' ] || [ $CARD_ID = '8016' ] || \
     [ $CARD_ID = '801a' ] ; then
        PRI=/lib/firmware/af010104.hex 
elif [ $CARD_ID = '800e' ] || [ $CARD_ID = '8015' ] || [ $CARD_ID = '8019' ] || \
     [ $CARD_ID = '801d' ] ; then
        PRI=/lib/firmware/pm010102.hex 
fi

DIR=/proc/net/hostap/wlan0  

if [ ! -d $DIR ]; then
    exit 1 
fi

if grep -q no_pri=1 $DIR/debug; then
    /usr/sbin/prism2_srec -gs wlan0 $PRI   
    /usr/sbin/prism2_srec -gp wlan0 $PRI 
fi

/usr/sbin/prism2_srec -rp wlan0 $STA

