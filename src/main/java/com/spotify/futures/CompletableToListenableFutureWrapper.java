/*
 * Copyright (c) 2013-2015 Spotify AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.spotify.futures;

import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

class CompletableToListenableFutureWrapper<V>
    extends AbstractFuture<V>
    implements ListenableFuture<V>, BiConsumer<V, Throwable> {

  private final CompletableFuture<V> future;

  CompletableToListenableFutureWrapper(final CompletableFuture<V> future) {
    this.future = future;
    future.whenComplete(this);
  }

  public CompletableFuture<V> unwrap() {
    return future;
  }

  @Override
  public void accept(final V v, final Throwable throwable) {
    if (throwable != null) {
      setException(throwable);
    } else {
      set(v);
    }
  }
}
