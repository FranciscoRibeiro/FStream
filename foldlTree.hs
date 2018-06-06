data Tree a = L a | B (Tree a) (Tree a)

--foldlT :: (b -> b -> b) -> (a -> b) -> Tree a -> b
--foldlT :: (String -> String -> String) -> (Integer -> String) -> Tree Integer -> String
foldlT b l tree = foldlAuxT tree l
    where
      --foldlAuxT :: Tree a -> (a -> b) -> b
      --foldlAuxT :: Tree Integer -> (Integer -> String) -> String
      foldlAuxT (L a) f = f a
      foldlAuxT (B s t) f = foldlAuxT s sFunc
          where
            sFunc = \x -> foldlAuxT t (\y -> f $ b (f x) (f y))

foldT :: (b -> b -> b) -> (a -> b) -> Tree a -> b
foldT b l (L a) = l a
foldT b l (B s t) = b (foldT b l s) (foldT b l t)

foldlT' :: (b -> b -> b) -> (a -> b) -> Tree a -> b
foldlT' b l tree = foldlTAux' [tree] Nothing
    where
      foldlTAux' [] (Just v) = v
      foldlTAux' ((L a) : ts) Nothing = foldlTAux' ts (Just (l a))
      foldlTAux' ((L a) : ts) (Just v) = foldlTAux' ts (Just (b v (l a)))
      foldlTAux' ((B s t) : ts) m = foldlTAux' (s:t:ts) m

tree = B (L 1) (L 2)
tree1 = L 1

sumT :: Tree Integer -> Integer
sumT t = sumRec t id
    where
      sumRec :: Tree Integer -> (Integer -> Integer) -> Integer
      sumRec (L a) f = f a
      sumRec (B s t) f = sumRec s sSum
        where
          sSum = \x -> sumRec t (\y -> f (x+y))

foldlT2 b l tree = foldlTAux2 [tree] l
    where
      foldlTAux2 [] f = f
      foldlTAux2 ((L a) : ts) f = foldlTAux2 ts (b.f)
      foldlTAux2 ((B s t) : ts) f = foldlTAux2 (s:t:ts) f

foldTTail _ l (L a) cont = cont (l a)
foldTTail b l (B s t) cont = -- foldTTail b l s (\a1 -> foldTTail b l t (\a2 -> b' a1 a2 cont))
                                foldTTail b l s (\a1 -> foldTTail b l t (\a2 -> cont (b a1 a2)))
    --where
        --b' x y = \cont' -> cont' (b x y)

g 0 = Left 0
g 1 = Left 1
g n = Right (n - 1, n - 2)

fib x = foldT (+) id (build g x)

build g x = unfoldT g x

--fib'' = foldT (+) id (build g)

        
unfoldT :: (t -> Either a (t, t)) -> t -> Tree a
unfoldT g x = case g x of
    Left a -> L a
    Right (c, d) -> B (unfoldT g c) (unfoldT g d)


--fib' x = g x (+) id

hyloT g l b seed = 
    case g seed of
        Left a -> l a
        Right (a1,a2) -> let
                         x = hyloT g l b a1
                         y = hyloT g l b a2
                        in b x y