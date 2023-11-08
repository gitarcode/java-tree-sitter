/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class ch_usi_si_seart_treesitter_Node */

#ifndef _Included_ch_usi_si_seart_treesitter_Node
#define _Included_ch_usi_si_seart_treesitter_Node
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    getChild
 * Signature: (IZ)Lch/usi/si/seart/treesitter/Node;
 */
JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_getChild
  (JNIEnv *, jobject, jint, jboolean);

/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    getChildByFieldName
 * Signature: (Ljava/lang/String;)Lch/usi/si/seart/treesitter/Node;
 */
JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_getChildByFieldName
  (JNIEnv *, jobject, jstring);

/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    getChildCount
 * Signature: (Z)I
 */
JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Node_getChildCount
  (JNIEnv *, jobject, jboolean);

/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    getChildren
 * Signature: (Lch/usi/si/seart/treesitter/Node;Z)[Lch/usi/si/seart/treesitter/Node;
 */
JNIEXPORT jobjectArray JNICALL Java_ch_usi_si_seart_treesitter_Node_getChildren
  (JNIEnv *, jclass, jobject, jboolean);

/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    getDescendant
 * Signature: (IIZ)Lch/usi/si/seart/treesitter/Node;
 */
JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_getDescendant__IIZ
  (JNIEnv *, jobject, jint, jint, jboolean);

/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    getDescendant
 * Signature: (Lch/usi/si/seart/treesitter/Point;Lch/usi/si/seart/treesitter/Point;Z)Lch/usi/si/seart/treesitter/Node;
 */
JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_getDescendant__Lch_usi_si_seart_treesitter_Point_2Lch_usi_si_seart_treesitter_Point_2Z
  (JNIEnv *, jobject, jobject, jobject, jboolean);

/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    getEndByte
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Node_getEndByte
  (JNIEnv *, jobject);

/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    getEndPoint
 * Signature: ()Lch/usi/si/seart/treesitter/Point;
 */
JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_getEndPoint
  (JNIEnv *, jobject);

/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    getFieldNameForChild
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_ch_usi_si_seart_treesitter_Node_getFieldNameForChild
  (JNIEnv *, jobject, jint);

/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    getFirstChildForByte
 * Signature: (IZ)Lch/usi/si/seart/treesitter/Node;
 */
JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_getFirstChildForByte
  (JNIEnv *, jobject, jint, jboolean);

/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    getNextSibling
 * Signature: (Z)Lch/usi/si/seart/treesitter/Node;
 */
JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_getNextSibling
  (JNIEnv *, jobject, jboolean);

/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    getPrevSibling
 * Signature: (Z)Lch/usi/si/seart/treesitter/Node;
 */
JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_getPrevSibling
  (JNIEnv *, jobject, jboolean);

/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    getParent
 * Signature: ()Lch/usi/si/seart/treesitter/Node;
 */
JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_getParent
  (JNIEnv *, jobject);

/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    getStartByte
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Node_getStartByte
  (JNIEnv *, jobject);

/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    getStartPoint
 * Signature: ()Lch/usi/si/seart/treesitter/Point;
 */
JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_getStartPoint
  (JNIEnv *, jobject);

/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    getSymbol
 * Signature: ()Lch/usi/si/seart/treesitter/Symbol;
 */
JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_getSymbol
  (JNIEnv *, jobject);

/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    getType
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_ch_usi_si_seart_treesitter_Node_getType
  (JNIEnv *, jobject);

/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    hasError
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_Node_hasError
  (JNIEnv *, jobject);

/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    isExtra
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_Node_isExtra
  (JNIEnv *, jobject);

/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    isMissing
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_Node_isMissing
  (JNIEnv *, jobject);

/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    isNamed
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_Node_isNamed
  (JNIEnv *, jobject);

/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    isNull
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_Node_isNull
  (JNIEnv *, jobject);

/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    walk
 * Signature: ()Lch/usi/si/seart/treesitter/TreeCursor;
 */
JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_walk__
  (JNIEnv *, jobject);

/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    walk
 * Signature: (Lch/usi/si/seart/treesitter/Query;)Lch/usi/si/seart/treesitter/QueryCursor;
 */
JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_walk__Lch_usi_si_seart_treesitter_Query_2
  (JNIEnv *, jobject, jobject);

/*
 * Class:     ch_usi_si_seart_treesitter_Node
 * Method:    equals
 * Signature: (Lch/usi/si/seart/treesitter/Node;Lch/usi/si/seart/treesitter/Node;)Z
 */
JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_Node_equals
  (JNIEnv *, jclass, jobject, jobject);

#ifdef __cplusplus
}
#endif
#endif
