/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util;

import java.util.logging.Level;

import com.jacob.com.Variant;

import vavi.util.Debug;


/**
 * ComUtil.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 040915 nsano initial version <br>
 */
public final class ComUtil {
    /** */
    private ComUtil() {}

    /** */
    public static Object toObject(Variant variant) {
Debug.println(Level.FINE, "variant: " + variant + ", " + Debug.getCallerMethod(1));
        if (variant == null) {
            return null;
        }
        short type = variant.getvt();
        String name;
        Object value;
        switch (type) {
        case Variant.VariantArray: // the VT_ARRAY modifier.
            name = "VariantArray";
            value = variant.toJavaObject(); // TODO
            break;
        case Variant.VariantBoolean: // the VT_BOOL variant type.
            name = "VariantBoolean";
            value = variant.getBoolean();
            break;
        case Variant.VariantByref: // the VT_BYREF modifier.
            name = "VariantByref";
            value = variant.toJavaObject();
            break;
        case Variant.VariantByte: // the VT_UI1 variant type.
            name = "VariantByte";
            value = variant.getByte();
            break;
        case Variant.VariantCurrency: // the VT_CY variant type.
            name = "VariantCurrency";
            value = variant.getCurrency();
            break;
        case Variant.VariantDate: // the VT_DATE variant type.
            name = "VariantDate";
Debug.println(Level.FINE, "date: " + variant + ": " + variant.getJavaDate());
            value = variant.getJavaDate();
            break;
        case Variant.VariantDispatch: // the VT_DISPATCH variant type.
            name = "VariantDispatch";
            value = variant.toDispatch();
            break;
        case Variant.VariantDouble: // the VT_R8 variant type.
            name = "VariantDouble";
            value = variant.getDouble();
            break;
        case Variant.VariantEmpty: // the VT_EMPTY variant type.
            name = "VariantEmpty";
            value = null;
            break;
        case Variant.VariantError: // the VT_ERROR variant type.
            name = "VariantError";
            value = new Exception("ComArchive: " + variant.getError());
            break;
        case Variant.VariantFloat: // the VT_R4 variant type.
            name = "VariantFloat";
            value = variant.getFloat();
            break;
        case Variant.VariantInt: // the VT_I4 variant type.
            name = "VariantInt";
            value = variant.getInt();
            break;
        case Variant.VariantNull: // the VT_NULL variant type.
            name = "VariantNull";
            value = variant.toJavaObject(); // TODO
            break;
        case Variant.VariantObject: // the VT_UNKNOWN variant type.
            name = "VariantObject";
            value = variant.toJavaObject();
            break;
        case Variant.VariantShort: // the VT_I2 variant type.
            name = "VariantShort";
            value = variant.getShort();
            break;
        case Variant.VariantString: // the VT_BSTR variant type.
            name = "VariantString";
            value = variant.getString();
            break;
        case Variant.VariantTypeMask: // Masks off variant type modifiers.
            name = "VariantTypeMask";
            value = variant.toJavaObject();
            break;
        case Variant.VariantVariant: //
            name = "VariantVariant";
            value = variant.toJavaObject();
            break;
        default:
            name = "VariantUnknown";
            value = variant.toJavaObject();
            break;
        }
Debug.println(Level.FINE, name + "(" + type + "): " + value);
        return value;
    }
}

/* */
